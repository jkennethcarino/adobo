package dev.jkcarino.adobo.patches.reddit.misc.outboundlink

import app.morphe.patcher.Fingerprint
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

private object AccountPreferencesToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf("AccountPreferences(over18=")
)

private object AccountToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf("Account(id=")
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
