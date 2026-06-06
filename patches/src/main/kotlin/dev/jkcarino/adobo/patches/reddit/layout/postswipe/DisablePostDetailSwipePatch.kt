package dev.jkcarino.adobo.patches.reddit.layout.postswipe

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.getReference

@Suppress("unused")
val disablePostDetailSwipePatch = bytecodePatch(
    name = "Disable post detail swipe",
    description = "Disables the horizontal swipe gesture used to navigate between posts."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    extendWith("extensions/reddit/frontpage.mpe")

    dependsOn(spoofCertificateHashPatch)

    execute {
        val extensionClass = DisablePostDetailSwipeFingerprint.method.definingClass

        PostDetailPagerFingerprint.method.apply {
            val achievementIndex = PostDetailPagerFingerprint.instructionMatches.last().index
            
            val screenPagerFieldIndex =
                UpdateLayoutSuppressionFingerprint.instructionMatches.first().index
            val screenPagerFieldInstruction =
                UpdateLayoutSuppressionFingerprint
                    .method
                    .getInstruction<TwoRegisterInstruction>(screenPagerFieldIndex)
            val screenPagerField =
                screenPagerFieldInstruction.getReference<FieldReference>()!!

            addInstructions(
                index = achievementIndex,
                smaliInstructions = """
                    iget-object v0, p0, ${screenPagerField.definingClass}->${screenPagerField.name}:${screenPagerField.type}
                    invoke-static {v0}, $extensionClass->apply(Landroid/view/View;)V
                """.trimIndent()
            )
        }

        setOf(
            ScreenPagerOnInterceptTouchEventFingerprint,
            ScreenPagerOnTouchEventFingerprint
        ).forEach { fingerprint ->
            fingerprint.method.apply {
                replaceInstructions(
                    index = 0,
                    smaliInstructions = """
                        invoke-static {p0}, $extensionClass->isSwipeEnabled(Landroid/view/View;)Z
                        move-result v0
                    """.trimIndent()
                )
            }
        }
    }
}
