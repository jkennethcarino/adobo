package dev.jkcarino.adobo.patches.reddit.layout.actions.crosspost

import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue

@Suppress("unused")
val hideCrosspostPatch = patch(
    name = "Hide crosspost",
    description = "Hides the crosspost on Reddit posts.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        if (packageMetadata.versionName >= "2026.34.0") {
            toStringFingerprints.forEach { fingerprint ->
                fingerprint.overrideFieldValue(false)
            }
        }

        RepostButtonFingerprint.method.returnEarly(null)
        ShouldAllowCrosspostsFingerprint.method.returnEarly(false)
    }
}
