package dev.jkcarino.adobo.patches.reddit.layout.relatedposts

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val hideRelatedPostsPatch = patch(
    name = "Hide related posts",
    description = "Hides the related posts section displayed at the bottom of a post."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        RelatedPostsFingerprint.method.returnEarly()
    }
}
