package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.string
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

internal val toStringFingerprints =
    setOf(
        "ActionCellFragment(id=",
        "PostActionScoreBarElement(linkId="
    ).map { dataClass ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string(dataClass),
                string(", shareCount="),
                fieldAccess(type = "Ljava/lang/Integer;")
            )
        )
    }

internal object GetShareCountFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getShareCount",
    returnType = "Ljava/lang/Long;",
    parameters = listOf()
)
