package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.returnEarly
import java.util.logging.Logger

@Suppress("unused")
val hideScoresPatch = bytecodePatch(
    name = "Hide upvote scores",
    description = "Hides the scores on Reddit posts and comments.",
    use = false
) {
    compatibleWith("com.reddit.frontpage")

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
            val scoreIndex = this.stringMatches.last().index + offset
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

            GetScoreFingerprint
                .match(LinkToStringFingerprint.classDef)
                .method
                .returnEarly(0)

            GetHideScoreFingerprint
                .match(LinkToStringFingerprint.classDef)
                .method
                .returnEarly(true)

            searchPostScoreToStringFingerprints.forEach { fingerprint ->
                fingerprint.updateScoreClassField(value = null)
            }
        }

        if (hideCommentScores!!) {
            SearchCommentScoreToStringFingerprint.updateScoreClassField(value = null)

            val scoreHiddenMethods = setOf(
                "getScoreHidden",
                "getIsScoreHidden",
                "isScoreHidden",
            )

            classDefForEach { classDef ->
                val hasAnalyticableCommentInterface = classDef
                    .interfaces
                    .any { it.endsWith("/AnalyticableComment;") }

                if (hasAnalyticableCommentInterface) {
                    classDef
                        .filterMethods { _, method ->
                            method.name in scoreHiddenMethods
                        }
                        .forEach { method ->
                            mutableClassDefBy(method.definingClass)
                                .findMutableMethodOf(method)
                                .returnEarly(true)
                        }
                }
            }
        }
    }
}
