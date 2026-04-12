package dev.jkcarino.adobo.patches.all.contentblocker.hosts

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.encodedValue.MutableStringEncodedValue
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.iface.value.StringEncodedValue
import com.android.tools.smali.dexlib2.immutable.value.ImmutableStringEncodedValue
import dev.jkcarino.adobo.util.getEncodedValue
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.transformation.transformationPatch
import java.util.logging.Logger

const val DEFAULT_REDIRECTION_IP = "0.0.0.0"

data class HostsBlockerConfig(
    val hostsBlocker: HostsBlocker,
    val redirectionIp: String = DEFAULT_REDIRECTION_IP,
    val wildcard: Boolean = true,
)

fun baseHostsBlockerPatch(
    configProvider: () -> HostsBlockerConfig,
) = bytecodePatch {
    val logger = Logger.getLogger(this::class.java.name)

    execute {
        val (hostsBlocker, redirectionIp, wildcard) = configProvider()
        val blockedHosts = mutableSetOf<String>()

        transformationPatch(
            fieldFilter = fieldFilter@{ _, field ->
                val encodedValue = field
                    .getEncodedValue<StringEncodedValue>()
                    ?: return@fieldFilter false
                val fieldValue = encodedValue.value

                hostsBlocker.isBlocked(fieldValue, wildcard)
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

                if (!hostsBlocker.isBlocked(string, wildcard)) {
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
