package dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

internal object SdkLoadAdFactoryFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "L",
    strings = listOf(
        "SDK disable",
        "SDK load ad factory should not be null",
    )
)

internal object LoadAdFingerprint : Fingerprint(
    definingClass = "Lcom/bytedance/sdk/openadsdk/api/",
    name = "loadAd",
    returnType = "V",
    filters = listOf(
        methodCall(
            definingClass = "Lcom/bytedance/sdk/openadsdk/api/factory/IADLoader;",
            name = "loadAd"
        )
    )
)

internal object ShowFingerprint : Fingerprint(
    definingClass = "Lcom/bytedance/sdk/openadsdk/component/",
    name = "show",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;")
)
