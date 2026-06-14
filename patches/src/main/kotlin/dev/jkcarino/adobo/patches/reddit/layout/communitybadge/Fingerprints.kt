package dev.jkcarino.adobo.patches.reddit.layout.communitybadge

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

private object CommentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("Comment(id="),
        string(", authorAchievementsBadge=")
    )
)

internal object GetAuthorAchievementsBadgeFingerprint : Fingerprint(
    classFingerprint = CommentToStringFingerprint,
    name = "getAuthorAchievementsBadge",
    returnType = "L",
    parameters = listOf()
)
