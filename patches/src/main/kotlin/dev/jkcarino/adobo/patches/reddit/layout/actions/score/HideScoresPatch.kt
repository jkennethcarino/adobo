package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue
import java.util.logging.Logger

@Suppress("unused")
val hideScoresPatch = bytecodePatch(
    name = "Hide upvote scores",
    description = "Hides the scores on Reddit posts and comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    val hidePostScores by booleanOption(
        key = "hidePostScores",
        default = true,
        title = "Hide post scores",
        description = "Removes scores from posts in feeds and search results.",
    )

    val hideCommentScores by booleanOption(
        key = "hideCommentScores",
        default = true,
        title = "Hide comment scores",
        description = "Removes scores from comments in threads and search results.",
    )

    execute {
        if (!hidePostScores!! && !hideCommentScores!!) {
            return@execute Logger
                .getLogger(this::class.java.name)
                .warning("No score visibility options are enabled. No changes made.")
        }

        if (hidePostScores!!) {
            ActionCellFragmentToStringFingerprint.overrideFieldValue(true)
            GetScoreFingerprint.method.returnEarly(0)
            GetHideScoreFingerprint.method.returnEarly(true)

            searchPostScoreToStringFingerprints.forEach { fingerprint ->
                fingerprint.overrideFieldValue(null)
            }
        }

        if (hideCommentScores!!) {
            SearchCommentScoreToStringFingerprint.overrideFieldValue(null)

            scoreHiddenFingerprints.forEach { fingerprint ->
                fingerprint.matchAll().forEach { match ->
                    match.method.instructionsOrNull ?: return@forEach
                    match.method.returnEarly(true)
                }
            }
        }
    }
}
