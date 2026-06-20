package dev.jkcarino.adobo.patches.reddit.layout.search.searchbar

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val hideProminentSearchBarPatch = bytecodePatch(
    name = "Hide prominent search bar",
    description = "Hides the new prominent search bar on the main screen. " +
        "Applicable only to version 2026.24.0 and earlier.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    val logger = Logger.getLogger(this::class.java.name)

    execute {
        // The prominent search bar won the A/B testing
        IsEnabledFingerprint.methodOrNull?.returnEarly()
            ?: logger.info(
                "Skipping 'Hide prominent search bar': " +
                    "applicable only to version 2026.24.0 and earlier."
            )
    }
}
