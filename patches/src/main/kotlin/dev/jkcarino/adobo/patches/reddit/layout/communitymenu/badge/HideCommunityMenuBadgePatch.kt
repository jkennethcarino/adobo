package dev.jkcarino.adobo.patches.reddit.layout.communitymenu.badge

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val hideCommunityMenuBadgePatch = bytecodePatch(
    name = "Hide community menu badge",
    description = "Hides the red notification badge on the community menu (hamburger menu)."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        CommunityMenuBadgeFingerprint.method.apply {
            val ifCheckIndex = CommunityMenuBadgeFingerprint.instructionMatches.first().index
            val isShownRegister = getInstruction<OneRegisterInstruction>(ifCheckIndex).registerA

            addInstruction(
                index = ifCheckIndex,
                smaliInstructions = "const/4 v$isShownRegister, 0x0"
            )
        }
    }
}
