package dev.jkcarino.adobo.patches.all.contentblocker.ads.mintegral

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object MBridgeSdkInitFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE),
    returnType = "V",
    strings = listOf(
        "com.mbridge.msdk",
        "INIT FAIL",
    )
)
