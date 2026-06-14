package dev.jkcarino.adobo.patches.reddit.layout.postswipe

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val disablePostDetailSwipePatch = bytecodePatch(
    name = "Disable post detail swipe",
    description = "Disables the horizontal swipe gesture used to navigate between posts.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        val screenPagerDefiningClass = CanScrollHorizontallyFingerprint.method.definingClass
        val fingerprint = pagerAdapterFingerprint(screenPagerDefiningClass)

        fingerprint.method.apply {
            val setAdapterIndex = fingerprint.instructionMatches.last().index
            val setAdapterInstruction = getInstruction<FiveRegisterInstruction>(setAdapterIndex)
            val screenPagerRegister = setAdapterInstruction.registerC
            val valueRegister = setAdapterInstruction.registerD

            addInstructions(
                index = setAdapterIndex + 1,
                smaliInstructions = """
                    const/4 v$valueRegister, 0x0
                    invoke-virtual {v$screenPagerRegister, v$valueRegister}, $screenPagerDefiningClass->setEnabled(Z)V
                    const/4 v$valueRegister, 0x1
                    invoke-virtual {v$screenPagerRegister, v$valueRegister}, $screenPagerDefiningClass->setNestedScrollingEnabled(Z)V
                """
            )
        }
    }
}
