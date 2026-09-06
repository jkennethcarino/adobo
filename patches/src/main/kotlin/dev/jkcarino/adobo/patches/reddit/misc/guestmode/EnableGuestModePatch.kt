package dev.jkcarino.adobo.patches.reddit.misc.guestmode

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val enableGuestModePatch = bytecodePatch(
    name = "Enable guest mode",
    description = "Enables browsing Reddit without signing in.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        IsIntroductionSeenFingerprint.method.returnEarly(true)
    }
}
