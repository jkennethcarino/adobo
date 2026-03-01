package dev.jkcarino.adobo.patches.all.contentblocker.ads.yandex

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.booleanOption
import com.android.tools.smali.dexlib2.AccessFlags
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.returnEarly

internal val disableYandexOption = booleanOption(
    key = "disableYandex",
    default = true,
    title = "Yandex Advertising Network",
    description = "Disable Banner, Interstitial, Native, Rewarded, App Open, InStream, and " +
        "Feed ad formats."
)

internal fun BytecodePatchContext.applyYandexPatch() = buildList {
    val adMethods = setOf(
        "loadAd",
        "loadAds",
        "prepareAd",
        "loadInstreamAd",
        "loadSlider",
        // Only present in YandexAdsLoader
        "requestAds",
        "setPlayer",
        "start",
        // Only present in FeedAd
        "preloadAd",
        "setLoadListener",
    )

    setOf(
        "Lcom/yandex/mobile/ads/appopenad/AppOpenAdLoader;",
        "Lcom/yandex/mobile/ads/banner/BannerAdView;",
        "Lcom/yandex/mobile/ads/feed/FeedAd;",
        "Lcom/yandex/mobile/ads/instream/InstreamAdBinder;",
        "Lcom/yandex/mobile/ads/instream/InstreamAdLoader;",
        "Lcom/yandex/mobile/ads/instream/exoplayer/YandexAdsLoader;",
        "Lcom/yandex/mobile/ads/instream/media3/YandexAdsLoader;",
        "Lcom/yandex/mobile/ads/interstitial/InterstitialAdLoader;",
        "Lcom/yandex/mobile/ads/nativeads/NativeAdLoader;",
        "Lcom/yandex/mobile/ads/nativeads/NativeBulkAdLoader;",
        "Lcom/yandex/mobile/ads/nativeads/SliderAdLoader;",
        "Lcom/yandex/mobile/ads/rewarded/RewardedAdLoader;",
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

    val inStreamInterfaces = setOf(
        "Lcom/yandex/mobile/ads/instream/inroll/Inroll;",
        "Lcom/yandex/mobile/ads/instream/player/ad/InstreamAdPlayer;",
        "Lcom/yandex/mobile/ads/instream/player/content/VideoPlayer;",
    )

    val inStreamAdMethods = setOf(
        "prepareAd",
        "playAd",
        "releaseAd",
        "resumeAd",
        "skipAd",
        "stopAd",
        "prepare",
        "play",
        "pause",
        "resume",
        "prepareVideo",
        "resumeVideo",
        "pauseVideo",
    )

    runCatching {
        classDefForEach { classDef ->
            if (inStreamInterfaces.any { it in classDef.interfaces }) {
                classDef
                    .filterMethods { _, method -> method.name in inStreamAdMethods }
                    .ifEmpty { throw PatchException("No custom InStream implementations found") }
                    .forEach { method ->
                        mutableClassDefBy(method.definingClass)
                            .findMutableMethodOf(method)
                            .returnEarly()
                    }
            }
        }
    }.also(::add)

    runCatching {
        val mutableClass = LoadFeedFingerprint.classDef

        mutableClass
            .filterMethods { _, method ->
                method.accessFlags == AccessFlags.PUBLIC.value or AccessFlags.FINAL.value
            }
            .forEach { method ->
                mutableClass
                    .findMutableMethodOf(method)
                    .returnEarly()
            }
    }.also(::add)
}
