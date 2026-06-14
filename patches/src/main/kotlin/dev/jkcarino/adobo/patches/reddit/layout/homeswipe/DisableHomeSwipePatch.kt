package dev.jkcarino.adobo.patches.reddit.layout.homeswipe

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val disableHomeSwipePatch = bytecodePatch(
    name = "Disable home feed swipe",
    description = "Disables the horizontal page swipe gesture used to switch feeds.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        ComposePagerScrollFingerprint.method.apply {
            val userScrollEnabledIndex =
                ComposePagerScrollFingerprint.instructionMatches.last().index
            val userScrollEnabledInstruction =
                getInstruction<TwoRegisterInstruction>(userScrollEnabledIndex)
            val userScrollEnabledRegister = userScrollEnabledInstruction.registerB

            addInstruction(
                index = 0,
                smaliInstructions = "const/16 v$userScrollEnabledRegister, 0x0"
            )
        }

        SwipingVariantFingerprint.method.apply {
            val isVariantEnabledIndex =
                SwipingVariantFingerprint.instructionMatches.first().index
            val variantIndex =
                SwipingVariantFingerprint.instructionMatches[4].index

            removeInstruction(variantIndex)
            removeInstruction(isVariantEnabledIndex)
        }
    }
}
