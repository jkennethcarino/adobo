package dev.jkcarino.adobo.patches.reddit.layout.actions.award

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue

@Suppress("unused")
val hideAwardsPatch = bytecodePatch(
    name = "Hide awards",
    description = "Hides the awards on Reddit posts and comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            fingerprint.overrideFieldValue(false)
        }

        IsGildableFingerprint.matchAll().forEach { match ->
            match.method.instructionsOrNull ?: return@forEach
            match.method.returnEarly(false)
        }
    }
}
