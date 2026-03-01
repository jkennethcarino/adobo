package dev.jkcarino.adobo.patches.reddit.shared

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

internal object LinkToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("Link(id=")
    )
)
