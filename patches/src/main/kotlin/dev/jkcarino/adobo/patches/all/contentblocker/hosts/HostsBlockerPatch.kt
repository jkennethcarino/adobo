package dev.jkcarino.adobo.patches.all.contentblocker.hosts

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.patcher.util.proxy.mutableTypes.encodedValue.MutableStringEncodedValue
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue
import com.android.tools.smali.dexlib2.immutable.value.ImmutableStringEncodedValue
import dev.jkcarino.adobo.util.getEncodedValue
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.transformation.transformationPatch
import java.io.File
import java.util.logging.Logger

@Suppress("unused")
val hostsBlockerPatch = bytecodePatch(
    name = "Block ads, trackers, and analytics",
    description = "Blocks ads, trackers, analytics, and unwanted content in apps and games " +
        "using a hosts file.",
    use = false
) {
    val logger = Logger.getLogger(this::class.java.name)

    val hostsOption by stringOption(
        key = "hosts",
        default = null,
        title = "Hosts file",
        description = "The hosts file containing hosts or domains you want to block.",
        required = true
    ) { filePath ->
        !filePath.isNullOrEmpty() && File(filePath.trim()).isFile
    }

    val redirectionIpOption by stringOption(
        key = "redirectionIp",
        title = "Redirection IP",
        default = "0.0.0.0",
        values = mapOf(
            "Default" to "0.0.0.0",
            "localhost" to "127.0.0.1"
        ),
        description = "The IP address to redirect blocked domains to. " +
            "This will be used with your hosts list to block content.",
        required = true
    ) { ipAddress ->
        // Basic validation but this doesn't validate whether the IP address is valid
        val ipAddressPattern = """^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$""".toRegex()
        !ipAddress.isNullOrEmpty() && ipAddress.matches(ipAddressPattern)
    }

    execute {
        val hostsFile = File(hostsOption!!.trim())
        val redirectionIp = redirectionIpOption!!
        val blockedHosts = mutableSetOf<String>()
        val hostsBlocker = HostsBlocker.fromFile(hostsFile)

        transformationPatch(
            fieldFilter = fieldFilter@{ _, field ->
                val encodedValue = field
                    .getEncodedValue<StringEncodedValue>()
                    ?: return@fieldFilter false
                val fieldValue = encodedValue.value

                hostsBlocker.isBlocked(fieldValue)
            },
            fieldTransform = fieldTransform@{ mutableField, _ ->
                val fieldValue = mutableField
                    .getEncodedValue<StringEncodedValue>()!!
                    .value

                val blockedHost = HostsBlocker
                    .extractHost(fieldValue)!!
                    .also(blockedHosts::add)

                val updatedHost = fieldValue.replace(
                    oldValue = blockedHost,
                    newValue = redirectionIp,
                    ignoreCase = true
                )

                mutableField.initialValue = MutableStringEncodedValue(
                    ImmutableStringEncodedValue(updatedHost)
                )
            },
            methodFilter = filter@{ _, _, instruction, instructionIndex ->
                val reference = instruction
                    .getReference<StringReference>()
                    ?: return@filter null
                val string = reference.string

                if (!hostsBlocker.isBlocked(string)) {
                    return@filter null
                }

                instructionIndex to string
            },
            methodTransform = { mutableMethod, entry ->
                val (index, string) = entry
                val register = mutableMethod
                    .getInstruction<OneRegisterInstruction>(index)
                    .registerA

                val blockedHost = HostsBlocker
                    .extractHost(string)!!
                    .also(blockedHosts::add)

                val updatedHost = string.replace(
                    oldValue = blockedHost,
                    newValue = redirectionIp,
                    ignoreCase = true
                )

                mutableMethod.replaceInstruction(
                    index = index,
                    smaliInstruction = """
                        const-string v$register, "$updatedHost"
                    """
                )
            }
        )

        blockedHosts.forEach { host ->
            logger.info("[Found] $host blocked.")
        }

        hostsBlocker.close()
        blockedHosts.clear()
    }
}
