package dev.jkcarino.adobo.patches.all.contentblocker.ads.unity

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.util.returnEarly
import app.morphe.util.findMutableMethodOf
import dev.jkcarino.adobo.util.defaultReturnEarly
import dev.jkcarino.adobo.util.filterMethods

internal val disableUnityOption = booleanOption(
    key = "disableUnity",
    default = true,
    title = "Unity",
    description = "Disable Banner, Interstitial, Native, and Rewarded Video ad formats."
)

internal fun applyUnityPatch() = buildList {
    val unityAdsClassDef = UnityAdsIsInitializedFingerprint.originalClassDefOrNull
    val adMethods = setOf(
        "initialize",
        "isInitialized",
        "isSupported",
        "load",
        "show",
        // Only present in BannerView
        "loadWebPlayer",
    )

    setOfNotNull(
        unityAdsClassDef?.type,
        "Lcom/unity3d/services/banners/BannerView;",
    ).forEach { definingClass ->
        runCatching {
            val mutableClass = mutableClassDefBy(definingClass)

            mutableClass
                .filterMethods { _, method -> method.name in adMethods }
                .forEach { method ->
                    mutableClass
                        .findMutableMethodOf(method)
                        .defaultReturnEarly()
                }
        }.also(::add)
    }

    runCatching {
        UnityServicesInitializeFingerprint.method.returnEarly()
    }.also(::add)
}
