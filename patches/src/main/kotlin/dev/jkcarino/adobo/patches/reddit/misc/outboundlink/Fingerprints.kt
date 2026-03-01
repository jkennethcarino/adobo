package dev.jkcarino.adobo.patches.reddit.misc.outboundlink

import app.morphe.patcher.Fingerprint

internal object AccountPreferencesToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf("AccountPreferences(over18=")
)

internal object GetAllowClickTrackingFingerprint : Fingerprint(
    name = "getAllowClickTracking",
    returnType = "Z"
)

internal object AccountToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf("Account(id=")
)

internal object GetOutboundClickTrackingFingerprint : Fingerprint(
    name = "getOutboundClickTracking",
    returnType = "Z"
)

internal object GetOutboundLinkFingerprint : Fingerprint(
    name = "getOutboundLink",
    returnType = "L",
    parameters = listOf()
)
