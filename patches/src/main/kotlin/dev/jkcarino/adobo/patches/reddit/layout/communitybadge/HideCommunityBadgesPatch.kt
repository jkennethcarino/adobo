package dev.jkcarino.adobo.patches.reddit.layout.communitybadge

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideCommunityBadgesPatch = bytecodePatch(
    name = "Hide user community badges",
    description = "Hides the user community badges on Reddit comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        GetAuthorAchievementsBadgeFingerprint.method.returnEarly()
    }
}
