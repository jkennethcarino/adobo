package dev.jkcarino.adobo.patches.reddit.layout.bottomnav.autohide

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val disableBottomNavAutoHidePatch = bytecodePatch(
    name = "Disable bottom nav auto-hide",
    description = "Prevents the bottom navigation bar from hiding when scrolling down.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        OnBottomNavStateChangeFingerprint.method.returnEarly()
    }
}
