package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.Fingerprint

internal object ActionCellFragmentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf(
        "ActionCellFragment(id=",
        ", shareCount=",
    )
)

internal object GetShareCountFingerprint : Fingerprint(
    name = "getShareCount",
    returnType = "Ljava/lang/Long;",
    parameters = listOf()
)
