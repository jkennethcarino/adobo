package dev.jkcarino.adobo.patches.ninegag.misc.forcedupdate

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.newInstance
import com.android.tools.smali.dexlib2.AccessFlags

internal object ShowCheckUpgradeDialogFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    filters = listOf(
        newInstance("/CheckUpgradeDialog;"),
        methodCall(name = "setCancelable"),
        methodCall(name = "show")
    )
)
