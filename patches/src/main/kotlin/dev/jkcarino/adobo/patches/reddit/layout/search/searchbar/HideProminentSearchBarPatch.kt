package dev.jkcarino.adobo.patches.reddit.layout.search.searchbar

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideProminentSearchBarPatch = bytecodePatch(
    name = "Hide prominent search bar",
    description = "Hides the new prominent search bar on the main screen.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        IsEnabledFingerprint.method.returnEarly()
    }
}
