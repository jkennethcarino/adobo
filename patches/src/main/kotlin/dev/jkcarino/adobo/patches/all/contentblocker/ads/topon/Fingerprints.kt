package dev.jkcarino.adobo.patches.all.contentblocker.ads.topon

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object AtSdkInitFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    strings = listOf("init: Context is null!", "anythink")
)

internal object AtRewardedVideoAdLoadManagerShowFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("4001", "", "No Cache.")
)
