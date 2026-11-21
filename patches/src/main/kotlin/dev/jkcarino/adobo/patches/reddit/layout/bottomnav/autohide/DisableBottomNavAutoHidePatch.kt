package dev.jkcarino.adobo.patches.reddit.layout.bottomnav.autohide

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val disableBottomNavAutoHidePatch = patch(
    name = "Disable bottom navigation bar auto-hide",
    description = "Prevents the bottom navigation bar from hiding when scrolling down.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        OnBottomNavStateChangeFingerprint.method.returnEarly()
    }
}
