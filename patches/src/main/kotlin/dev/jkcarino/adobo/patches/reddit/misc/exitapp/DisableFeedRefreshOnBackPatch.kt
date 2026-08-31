package dev.jkcarino.adobo.patches.reddit.misc.exitapp

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import java.util.logging.Logger

@Suppress("unused")
val disableFeedRefreshOnBackPatch = bytecodePatch(
    name = "Disable home feed refresh on back to exit",
    description = "Disables the back-to-top scroll and home feed refresh on " +
        "back navigation to exit.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        if (packageMetadata.versionName < "2026.18.0") {
            return@execute Logger
                .getLogger(this::class.java.name)
                .warning(
                    "Skipping 'Disable home feed refresh on back to exit': " +
                        "applicable only to version 2026.18.0 and later."
                )
        }

        ShowToastFingerprint.method.apply {
            val shouldShowToastIndex =
                ShowToastFingerprint.instructionMatches.first().index
            val shouldShowToastRegister =
                getInstruction<TwoRegisterInstruction>(shouldShowToastIndex).registerA

            replaceInstruction(
                index = shouldShowToastIndex,
                smaliInstruction = "const/4 v$shouldShowToastRegister, 0x1"
            )
        }

        InvokeFingerprint.method.apply {
            val isFeedAtTopIndex = InvokeFingerprint.instructionMatches[1].index
            val isFeedAtTopRegister =
                getInstruction<OneRegisterInstruction>(isFeedAtTopIndex).registerA

            addInstruction(
                index = isFeedAtTopIndex + 1,
                smaliInstructions = "const/4 v$isFeedAtTopRegister, 0x0"
            )
        }
    }
}
