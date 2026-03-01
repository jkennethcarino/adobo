package dev.jkcarino.adobo.patches.all.contentblocker.ads.meta

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.booleanOption
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.returnEarly

internal val disableMetaAudienceNetworkOption = booleanOption(
    key = "disableMetaAudienceNetwork",
    default = true,
    title = "Meta Audience Network",
    description = "Disable Banner, Medium rectangle, Interstitial, Native, Native Banner, " +
        "Rewarded Video, and Rewarded Interstitial ad formats."
)

internal fun BytecodePatchContext.applyMetaAudienceNetworkPatch() = buildList {
    val adMethods = setOf(
        "loadAd",
        // Only present in interstitial ads
        "isAdLoaded",
        "show",
    )

    setOf(
        "Lcom/facebook/ads/AdView;",
        "Lcom/facebook/ads/NativeAdBase;",
        "Lcom/facebook/ads/RewardedInterstitialAd;",
        "Lcom/facebook/ads/RewardedVideoAd;",
    ).forEach { definingClass ->
        runCatching {
            val mutableClass = mutableClassDefBy(definingClass)

            mutableClass
                .filterMethods { _, method -> method.name in adMethods }
                .forEach { method ->
                    mutableClass
                        .findMutableMethodOf(method)
                        .returnEarly()
                }
        }.also(::add)
    }

    runCatching {
        InitializeFingerprint.method.returnEarly()
    }.also(::add)
}
