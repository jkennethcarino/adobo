package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

internal object ActionCellFragmentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("ActionCellFragment(id="),
        string(", shareCount=")
    )
)

internal object GetShareCountFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getShareCount",
    returnType = "Ljava/lang/Long;",
    parameters = listOf()
)
