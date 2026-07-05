package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
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
        fun Fingerprint.updateScoreClassField(value: Any?, offset: Int = 2) {
            val scoreIndex = this.instructionMatches.last().index + offset
            val scoreInstruction = this.method.getInstruction<TwoRegisterInstruction>(scoreIndex)
            val scoreFieldReference = scoreInstruction.getReference<FieldReference>()!!

            updateClassField(
                classDef = this.classDef,
                fieldReference = scoreFieldReference,
                value = value
            )
        }

        if (!hidePostScores!! && !hideCommentScores!!) {
            return@execute Logger
                .getLogger(this::class.java.name)
                .warning("No score visibility options are enabled. No changes made.")
        }

        if (hidePostScores!!) {
            ActionCellFragmentToStringFingerprint.updateScoreClassField(value = true)
            GetScoreFingerprint.method.returnEarly(0)
            GetHideScoreFingerprint.method.returnEarly(true)

            searchPostScoreToStringFingerprints.forEach { fingerprint ->
                fingerprint.updateScoreClassField(value = null)
            }
        }

        if (hideCommentScores!!) {
            SearchCommentScoreToStringFingerprint.updateScoreClassField(value = null)

            scoreHiddenFingerprints.forEach { fingerprint ->
                fingerprint.matchAll().forEach { match ->
                    match.method.instructionsOrNull ?: return@forEach
                    match.method.returnEarly(true)
                }
            }
        }
    }
}
