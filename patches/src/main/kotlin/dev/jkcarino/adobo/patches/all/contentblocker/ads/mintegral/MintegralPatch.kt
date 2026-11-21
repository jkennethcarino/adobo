package dev.jkcarino.adobo.patches.all.contentblocker.ads.mintegral

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.util.returnEarly
import app.morphe.util.findMutableMethodOf
import dev.jkcarino.adobo.util.defaultReturnEarly
import dev.jkcarino.adobo.util.filterMethods

internal val disableMintegralOption = booleanOption(
    key = "disableMintegral",
    default = true,
    title = "Mintegral",
    description = "Disable Banner, Interstitial, Rewarded Video, Native, and Splash ad formats."
)

internal fun applyMintegralPatch() = buildList {
    val adMethods = setOf(
        "init",
        "load",
        "loadFromBid",
        "isReady",
        // Only present in MBBidInterstitialVideoHandler, MBInterstitialVideoHandler,
        // and MBNewInterstitialHandler
        "isBidReady",
        "loadFormSelfFilling",
        "showFromBid",
        // Only present in MBRewardVideoHandler
        "show",
        // Only present in MBSplashView
        "isImageReady",
        "isVideoReady",
        // Only present in MBNativeAdvancedHandler
        "loadByToken",
    )

    setOf(
        "Lcom/mbridge/msdk/interstitialvideo/out/MBBidInterstitialVideoHandler;",
        "Lcom/mbridge/msdk/interstitialvideo/out/MBInterstitialVideoHandler;",
        "Lcom/mbridge/msdk/newinterstitial/out/MBNewInterstitialHandler",
        "Lcom/mbridge/msdk/newout/MBBidRewardVideoHandler;",
        "Lcom/mbridge/msdk/newout/MBRewardVideoHandler;",
        "Lcom/mbridge/msdk/out/MBBannerView;",
        "Lcom/mbridge/msdk/out/MBNativeAdvancedHandler;",
        "Lcom/mbridge/msdk/out/MBSplashHandler;",
        "Lcom/mbridge/msdk/splash/view/MBSplashView;",
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
        MBridgeSdkInitFingerprint.method.returnEarly()
    }.also(::add)
}
