package dev.jkcarino.adobo.patches.all.contentblocker.ads.admob

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.util.returnEarly

internal val disableGoogleAdMobOption = booleanOption(
    key = "disableGoogleAdMob",
    default = true,
    title = "Google AdMob",
    description = "Disable Banner, Interstitial, Native, Rewarded, Rewarded Interstitial, and " +
        "App Open ad formats."
)

internal fun applyGoogleAdMobPatch() = buildList {
    setOf(
        abstractAdViewAdapterFingerprints,
        mediationFingerprints,
    ).flatten().forEach { fingerprint ->
        runCatching {
            fingerprint.method.returnEarly()
        }.also(::add)
    }

    setOf(
        AdLoadFingerprint,
        AdLoaderLoadAdFingerprint,
        ShowFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.matchAll().forEach { match ->
                match.method.returnEarly()
            }
        }.also(::add)
    }

    setOf(
        AppOpenAdLoadFingerprint,
        BaseAdViewFingerprint,
        BannerAdFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.method.returnEarly()
        }.also(::add)
    }
}
