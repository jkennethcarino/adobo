package dev.jkcarino.adobo.patches.google.gboard.incognito.voicetyping

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableVoiceTypingPatch = bytecodePatch(
    name = "Enable voice typing in incognito",
    description = "Enables voice typing in incognito mode.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        EnableVoiceTypingFingerprint.method.apply {
            val isIncognitoEnabledIndex =
                EnableVoiceTypingFingerprint.instructionMatches.last().index
            val isIncognitoEnabledInstruction =
                getInstruction<OneRegisterInstruction>(isIncognitoEnabledIndex)
            val isIncognitoEnabledRegister = isIncognitoEnabledInstruction.registerA

            addInstruction(
                index = isIncognitoEnabledIndex,
                smaliInstructions = "const/4 v$isIncognitoEnabledRegister, 0x0"
            )
        }
    }
}
