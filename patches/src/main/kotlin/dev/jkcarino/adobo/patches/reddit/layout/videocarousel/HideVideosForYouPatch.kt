package dev.jkcarino.adobo.patches.reddit.layout.videocarousel

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import java.util.logging.Logger

@Suppress("unused")
val hideVideosForYouPatch = bytecodePatch(
    name = "Hide videos for you",
    description = "Hides the \"Videos for you\" or video carousel recommendation section " +
        "between feed posts.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        if (packageMetadata.versionName < "2026.40.0") {
            return@execute Logger
                .getLogger(this::class.java.name)
                .warning(
                    "Skipping 'Hide videos for you carousel': " +
                        "applicable only to version 2026.40.0 and later."
                )
        }

        VideoCarouselConverterFingerprint.method.returnEarly(null)
    }
}
