package dev.jkcarino.adobo.patches.google.gboard.misc.incognito

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val alwaysIncognitoModePatch = bytecodePatch(
    name = "Always-incognito mode",
    description = "Always opens Gboard in incognito mode to disable typing history collection " +
        "and personalization.",
    use = true
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(bypassSignaturePatch)

    execute {
        val method = IsIncognitoModeFingerprint.methodOrNull
            ?: IsIncognitoModeV2Fingerprint.methodOrNull
            ?: IsIncognitoModeInlinedFingerprint.methodOrNull
            ?: throw PatchException("Failed to force-enable incognito mode.")

        when (method) {
            IsIncognitoModeInlinedFingerprint.methodOrNull -> {
                val patternResult = IsIncognitoModeInlinedFingerprint.instructionMatches
                val requestIncognitoModeIndex = patternResult.last().index
                val isIncognitoModeIndex = requestIncognitoModeIndex - 1

                val requestIncognitoModeInstruction =
                    method.getInstruction<FiveRegisterInstruction>(requestIncognitoModeIndex)
                val isIncognitoModeRegister = requestIncognitoModeInstruction.registerD

                method.replaceInstruction(
                    index = isIncognitoModeIndex,
                    smaliInstruction = "const/4 v$isIncognitoModeRegister, 0x1"
                )
            }
            else -> {
                method.returnEarly(true)
            }
        }
    }
}
