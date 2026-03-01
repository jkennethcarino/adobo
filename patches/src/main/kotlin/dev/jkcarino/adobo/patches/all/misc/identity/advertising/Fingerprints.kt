package dev.jkcarino.adobo.patches.all.misc.identity.advertising

import app.morphe.patcher.Fingerprint

internal object GetInfoInternalFingerprint : Fingerprint(
    returnType = "L",
    strings = listOf(
        "Calling this from your main thread can lead to deadlock",
        "AdvertisingIdClient",
        "GMS remote exception",
        "Remote exception",
    )
)
