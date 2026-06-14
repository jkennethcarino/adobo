package dev.jkcarino.adobo.patches.reddit.misc.homeredirect

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val disableHomeRedirectPatch = bytecodePatch(
    name = "Disable home screen redirect",
    description = "Disables the automatic redirect to the home screen " +
        "after being away from the app or returning from the background."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        AppResumeRedirectFingerprint.methodOrNull?.returnEarly() ?: run {
            OnResumeFingerprint.method.apply {
                val onResumeIndex = OnResumeFingerprint.instructionMatches.first().index

                addInstruction(
                    index = onResumeIndex + 1,
                    smaliInstructions = "return-void"
                )
            }
        }
    }
}
