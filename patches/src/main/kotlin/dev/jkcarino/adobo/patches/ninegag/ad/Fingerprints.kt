package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string

internal object InitializeFingerprint : Fingerprint(
    filters = listOf(
        string("9Ads"),
        string("MobileAds.initialize started")
    )
)

private object HandleIdentityFingerprint : Fingerprint(
    returnType = "V",
    filters = listOf(
        string("EUID"),
        string("handleIdentity: Adding ")
    )
)

internal object ShouldShowAdsFingerprint : Fingerprint(
    classFingerprint = HandleIdentityFingerprint,
    returnType = "Z",
    parameters = listOf()
)
