package dev.jkcarino.adobo.patches.reddit.layout.systemnavbar

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.addInstructionsAtControlFlowLabel
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val makeSystemNavBarTransparentPatch = bytecodePatch(
    name = "Make system navigation bar transparent",
    description = "Sets the system navigation bar to fully transparent.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        EdgeToEdgeSetUpFingerprint.method.apply {
            val setNavigationBarContrastEnforcedIndex =
                EdgeToEdgeSetUpFingerprint.instructionMatches.last().index
            val setNavigationBarContrastEnforcedRegister =
                getInstruction<FiveRegisterInstruction>(setNavigationBarContrastEnforcedIndex)
                    .registerD

            addInstructionsAtControlFlowLabel(
                insertIndex = setNavigationBarContrastEnforcedIndex,
                instructions = "const/4 v$setNavigationBarContrastEnforcedRegister, 0x0"
            )

            val setNavigationBarColorIndex =
                EdgeToEdgeSetUpFingerprint.instructionMatches.first().index
            val setNavigationBarColorRegister =
                getInstruction<FiveRegisterInstruction>(setNavigationBarColorIndex)
                    .registerD

            addInstructionsAtControlFlowLabel(
                insertIndex = setNavigationBarColorIndex,
                instructions = "const/4 v$setNavigationBarColorRegister, 0x0"
            )
        }
    }
}
