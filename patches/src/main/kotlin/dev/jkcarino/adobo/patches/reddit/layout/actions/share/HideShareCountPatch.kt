package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue

@Suppress("unused")
val hideShareCountPatch = bytecodePatch(
    name = "Hide share count",
    description = "Hides the share count on Reddit posts.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            fingerprint.overrideFieldValue(null)
        }

        GetShareCountFingerprint.method.returnEarly(null)
    }
}
