package dev.jkcarino.adobo.patches.reddit.layout.flair

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue

@Suppress("unused")
val hideUserFlairsPatch = bytecodePatch(
    name = "Hide user flairs",
    description = "Hides the user flairs on Reddit comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        setOf(
            commentPresentationModelToStringFingerprints,
            commentToStringFingerprints
        ).flatten().forEach { fingerprint ->
            fingerprint.overrideFieldValue(null)
        }

        setOf(
            GetAuthorFlairTextFingerprint,
            GetAuthorFlairRichTextFingerprint
        ).forEach { fingerprint ->
            fingerprint.matchAll().forEach { match ->
                match.method.instructionsOrNull ?: return@forEach
                match.method.returnEarly(null)
            }
        }
    }
}
