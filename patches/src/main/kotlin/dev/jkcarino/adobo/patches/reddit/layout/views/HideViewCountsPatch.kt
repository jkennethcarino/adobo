package dev.jkcarino.adobo.patches.reddit.layout.views

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue

@Suppress("unused")
val hideViewCountsPatch = patch(
    name = "Hide post view counts",
    description = "Removes the \"views\" indicator from the post metadata."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            fingerprint.overrideFieldValue(null)
        }

        GetViewCountFingerprint.method.returnEarly(null)
    }
}
