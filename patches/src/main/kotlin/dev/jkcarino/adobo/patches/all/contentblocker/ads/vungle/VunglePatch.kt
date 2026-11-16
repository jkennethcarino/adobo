package dev.jkcarino.adobo.patches.all.contentblocker.ads.vungle

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.util.findMutableMethodOf
import dev.jkcarino.adobo.util.defaultReturnEarly
import dev.jkcarino.adobo.util.filterMethods

internal val disableVungleOption = booleanOption(
    key = "disableVungle",
    default = true,
    title = "Liftoff Monetize",
    description = "Disable Banner, Interstitial, Native, Rewarded, and App Open ad formats."
)

internal fun applyVunglePatch() = buildList {
    val adMethods = setOf(
        "load",
        "loadAd",
        "canPlayAd",
    )

    setOf(
        "Lcom/vungle/ads/internal/AdInternal;",
        "Lcom/vungle/ads/BaseFullscreenAd;",
        "Lcom/vungle/ads/BaseAd;",
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
}
