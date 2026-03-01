package dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object SdkLoadAdFactoryFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "L",
    strings = listOf(
        "SDK disable",
        "SDK load ad factory should not be null",
    )
)
