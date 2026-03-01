package dev.jkcarino.adobo.patches.reddit.layout.actions.score

import app.morphe.patcher.Fingerprint

internal val searchPostScoreToStringFingerprints =
    setOf(
        "PostContentFragment(__typename=",
        "SearchPostContentFragment(__typename=",
    ).map { prefix ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            strings = listOf(prefix, ", score=")
        )
    }

internal object ActionCellFragmentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf(
        ", isScoreHidden=",
        "ActionCellFragment(id=",
    )
)

internal object GetScoreFingerprint : Fingerprint(
    name = "getScore",
    returnType = "I",
    parameters = listOf()
)

internal object GetHideScoreFingerprint : Fingerprint(
    name = "getHideScore",
    returnType = "Z",
    parameters = listOf()
)

internal object SearchCommentScoreToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf(
        "SearchComment(commentId=",
        ", score=",
    )
)
