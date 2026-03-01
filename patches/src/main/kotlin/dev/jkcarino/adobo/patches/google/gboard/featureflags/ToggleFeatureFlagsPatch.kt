package dev.jkcarino.adobo.patches.google.gboard.featureflags

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringsOption
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.util.toHexString
import java.util.logging.Logger

@Suppress("unused")
val toggleFeatureFlagsPatch = bytecodePatch(
    name = "Toggle feature flags",
    description = "Toggles Gboard feature flags to enable or disable " +
        "experimental or hidden features.",
    use = false
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(bypassSignaturePatch)

    val logger = Logger.getLogger(this::class.java.name)

    val featureFlags by stringsOption(
        key = "featureFlags",
        default = null,
        title = "Feature flags",
        description = "The Gboard feature flags to toggle, " +
            "such as experimental or hidden features.",
        required = true
    ) { flags ->
        val flagsRegex = """^[A-Za-z0-9_-]+$""".toRegex()
        !flags.isNullOrEmpty() && flags.all { it.matches(flagsRegex) }
    }

    val enableFlags by booleanOption(
        key = "enableFlags",
        default = true,
        title = "Enable feature flags",
        description = "Enables or disables all specified feature flags."
    )

    execute {
        featureFlags!!.forEach { flag ->
            val fingerprint = featureFlagFingerprint(flag.trim())

            runCatching {
                fingerprint.method.apply {
                    val isEnabledIndex = fingerprint.instructionMatches.last().index
                    val isEnabledInstruction =
                        getInstruction<OneRegisterInstruction>(isEnabledIndex)
                    val isEnabledRegister = isEnabledInstruction.registerA
                    val enabled = enableFlags.toHexString()

                    replaceInstruction(
                        index = isEnabledIndex,
                        smaliInstruction = "const/4 v$isEnabledRegister, $enabled"
                    )
                }
            }.onSuccess {
                val state = if (enableFlags!!) "on" else "off"
                logger.info("[Found] \"$flag\" toggled $state.")
            }.onFailure {
                logger.info("[Skipped] \"$flag\" was not found. No changes applied.")
            }
        }
    }
}
