package dev.jkcarino.adobo.patches.reddit.layout.search.answers

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val hideAnswersPreviewPatch = patch(
    name = "Hide search answers preview",
    description = "Hides the Reddit Answers preview section at the top of search results.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        AnswersPreviewStreamingSectionFingerprint.method.returnEarly()
        AnswersPreviewSectionFingerprint.method.returnEarly()
    }
}
