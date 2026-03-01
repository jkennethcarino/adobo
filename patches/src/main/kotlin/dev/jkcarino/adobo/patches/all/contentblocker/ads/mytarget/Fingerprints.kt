package dev.jkcarino.adobo.patches.all.contentblocker.ads.mytarget

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal object MyTargetManagerInitSdkFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    strings = listOf(
        "MyTarget cannot be initialized due to a null application context",
        "MyTarget initialization",
    )
)

internal object OnAdLoadExecutorFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf(
        "AsyncCommand",
        "Can't use onAdLoadExecutor - sdk initialize not finished",
    )
)

internal object PromoCardRecyclerViewSetAdapterFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    strings = listOf(
        "PromoCardRecyclerView: You must use setPromoCardAdapter(PromoCardAdapter) method with custom CardRecyclerView",
    )
)

internal val adLoaderFingerprints =
    setOf(
        "MyTargetView: Doesn't support multiple load",
        "BaseInterstitialAd: Interstitial/Rewarded doesn't support multiple load",
        "InstreamAd: Doesn't support multiple load",
        "InstreamAudioAd: Doesn't support multiple load",
        "NativeAd: Doesn't support multiple load",
        "NativeAppwallAd: Appwall ad doesn't support multiple load",
        "NativeBannerAd: Doesn't support multiple load",
        "NativeAdLoader: Invalid bannersCount < 1, bannersCount set to ",
        "NativeBannerAdLoader: Invalid bannersCount < 1, bannersCount set to ",
    ).map { string ->
        Fingerprint(
            returnType = "V",
            strings = listOf(string)
        )
    }
