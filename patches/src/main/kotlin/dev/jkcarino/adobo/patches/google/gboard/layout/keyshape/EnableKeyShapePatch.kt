package dev.jkcarino.adobo.patches.google.gboard.layout.keyshape

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableKeyShapePatch = bytecodePatch(
    name = "Enable key shape selection",
    description = "Enables an option to select a key shape from the theme.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        MorePillKeysFingerprint.method.apply {
            val isFlagEnabledIndex = MorePillKeysFingerprint.instructionMatches.last().index
            val isEnabledIndex = MorePillKeysFingerprint.instructionMatches[1].index
            val isEnabledInstruction = getInstruction<FiveRegisterInstruction>(isEnabledIndex)
            val isEnabledRegister = isEnabledInstruction.registerD

            addInstruction(
                index = isFlagEnabledIndex + 1,
                smaliInstructions = "const/4 v$isEnabledRegister, 0x0"
            )
            addInstruction(
                index = isEnabledIndex,
                smaliInstructions = "const/4 v$isEnabledRegister, 0x1"
            )
        }
    }
}
