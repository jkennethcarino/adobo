package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
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
                string(", score=")
            )
        )
    }

internal object ActionCellFragmentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string(", isScoreHidden="),
        string("ActionCellFragment(id=")
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
        string(", score=")
    )
)
