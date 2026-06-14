package dev.jkcarino.adobo.patches.reddit.misc.outboundlink

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

private object AccountPreferencesToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("AccountPreferences(over18=")
    )
)

private object AccountToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("Account(id="),
        string(", outboundClickTracking=")
    )
)

internal object GetAllowClickTrackingFingerprint : Fingerprint(
    classFingerprint = AccountPreferencesToStringFingerprint,
    name = "getAllowClickTracking",
    returnType = "Z"
)

internal object GetOutboundClickTrackingFingerprint : Fingerprint(
    classFingerprint = AccountToStringFingerprint,
    name = "getOutboundClickTracking",
    returnType = "Z"
)

internal object GetOutboundLinkFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getOutboundLink",
    returnType = "L",
    parameters = listOf()
)
