package dev.jkcarino.adobo.patches.reddit.layout.communityhighlights

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.string

internal object SubredditInfoByIdToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("SubredditInfoById(__typename="),
        string(", highlightedPostsModeratorsInfoFragment="),
        fieldAccess(type = "L")
    )
)

internal object InvokeFingerprint : Fingerprint(
    name = "invoke",
    filters = listOf(
        string("collapse_expand_highlight"),
        string("content caret")
    )
)

internal object LoadedToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("Loaded(highlightedItems=")
    )
)
