package dev.jkcarino.adobo.patches.all.contentblocker.ads.meta

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object InitializeFingerprint : Fingerprint(
    definingClass = "Lcom/facebook/ads/internal/dynamicloading/DynamicLoaderFactory;",
    name = "initialize",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "L", "L", "Z")
)
