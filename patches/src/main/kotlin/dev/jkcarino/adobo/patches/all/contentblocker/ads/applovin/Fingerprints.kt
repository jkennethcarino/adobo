package dev.jkcarino.adobo.patches.all.contentblocker.ads.applovin

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object InterstitialAdDialogToStringFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "Ljava/lang/String;",
    strings = listOf("AppLovinInterstitialAdDialog{}")
)
