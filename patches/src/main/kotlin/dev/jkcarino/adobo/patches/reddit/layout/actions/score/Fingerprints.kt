package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionFilter
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

internal val searchPostScoreToStringFingerprints =
    setOf(
        "PostContentFragment(__typename=",
        "SearchPostContentFragment(__typename=",
    ).map { prefix ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string(prefix),
                string(", score="),
                opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
                opcode(Opcode.IGET_OBJECT, MatchAfterImmediately())
            )
        )
    }

internal val scoreHiddenFingerprints =
    setOf(
        "getScoreHidden",
        "isScoreHidden",
    ).map { methodName ->
        Fingerprint(
            interfaces = listOf("/AnalyticableComment;"),
            name = methodName
        )
    }

private fun scoreHiddenToStringFingerprint(
    vararg filters: InstructionFilter,
) = Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = filters.toList()
)

internal val postScoreHiddenToStringFingerprints =
    listOf(
        scoreHiddenToStringFingerprint(
            string(", isScoreHidden="),
            string("ActionCellFragment(id="),
            opcode(Opcode.IGET_OBJECT, MatchAfterImmediately()),
            opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately())
        ),
        scoreHiddenToStringFingerprint(
            string("PostPreviewVoteInfoFragment(score="),
            string(", isScoreHidden="),
            opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
            opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately())
        ),
        scoreHiddenToStringFingerprint(
            string("MetricCellFragment(id="),
            string(", isScoreHidden="),
            opcode(Opcode.IGET),
            opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately())
        )
    )

internal object GetScoreFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getScore",
    returnType = "I",
    parameters = listOf()
)

internal object GetHideScoreFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getHideScore",
    returnType = "Z",
    parameters = listOf()
)

internal object SearchCommentScoreToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("SearchComment(commentId="),
        string(", score="),
        opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
        opcode(Opcode.IGET, MatchAfterImmediately())
    )
)
