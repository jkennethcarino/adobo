package dev.jkcarino.adobo.patches.google.gboard.featureflags

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstructionOrNull
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.util.indexOfFirstInstructionReversedOrThrow
import app.morphe.util.toHexString
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11n
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

context(_: BytecodePatchContext)
fun toggleFeatureFlag(
    flag: String,
    enabled: Boolean,
) {
    val fingerprint = booleanFlagFingerprint(flag)

    fingerprint.method.apply {
        val isEnabledIndex = fingerprint.instructionMatches[1].index
        val isEnabledInstruction = getInstructionOrNull(isEnabledIndex)
        val isEnabledRegister =
            when (isEnabledInstruction) {
                is OneRegisterInstruction -> isEnabledInstruction.registerA
                is FiveRegisterInstruction -> isEnabledInstruction.registerD
                else -> throw IllegalStateException(
                    "Unexpected instruction type at index $isEnabledIndex"
                )
            }

        val isEnabledOriginalIndex =
            indexOfFirstInstructionReversedOrThrow(
                startIndex = isEnabledIndex,
                filter = {
                    this is BuilderInstruction11n &&
                        this.registerA == isEnabledRegister
                }
            )
        val isEnabledOriginalValue =
            getInstruction<BuilderInstruction11n>(isEnabledOriginalIndex)
                .narrowLiteral

        val flagFieldIndex = fingerprint.instructionMatches.last().index

        // Keep the original value to avoid affecting flags
        // that use the same register
        addInstruction(
            index = flagFieldIndex + 1,
            smaliInstructions = "const/4 v$isEnabledRegister, $isEnabledOriginalValue"
        )

        val value = enabled.toHexString()
        val smaliInstruction = "const/4 v$isEnabledRegister, $value"

        if (isEnabledInstruction is OneRegisterInstruction) {
            replaceInstruction(
                index = isEnabledIndex,
                smaliInstruction = smaliInstruction
            )
        } else {
            addInstruction(
                index = isEnabledIndex,
                smaliInstructions = smaliInstruction
            )
        }
    }
}

context(_: BytecodePatchContext)
fun toggleFeatureFlag(
    flag: String,
    value: String,
) {
    val fingerprint = stringFlagFingerprint(flag)

    fingerprint.method.apply {
        val targetIndex = fingerprint.instructionMatches[1].index
        val register = getInstruction<OneRegisterInstruction>(targetIndex).registerA

        replaceInstruction(
            index = targetIndex,
            smaliInstruction = "const-string v$register, $value"
        )
    }
}
