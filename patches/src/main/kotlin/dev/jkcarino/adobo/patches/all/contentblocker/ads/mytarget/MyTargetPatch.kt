package dev.jkcarino.adobo.patches.all.contentblocker.ads.mytarget

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.booleanOption
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.returnEarly

internal val disableMyTargetOption = booleanOption(
    key = "disableMyTarget",
    default = true,
    title = "myTarget",
    description = "Disable Banner, Interstitial, Rewarded, Native, Native Banner, " +
        "In-Stream Video and Audio, and Carousel ad formats."
)

internal fun BytecodePatchContext.applyMyTargetPatch() = buildList {
    val adLoaderAdMethods = setOf(
        "handleResult",
        "load",
        "loadFromBid",
        "show",
    )
    adLoaderFingerprints.forEach { fingerprint ->
        runCatching {
            val mutableClass = fingerprint.classDef

            mutableClass
                .filterMethods { _, method -> method.name in adLoaderAdMethods }
                .forEach { method ->
                    mutableClass
                        .findMutableMethodOf(method)
                        .returnEarly()
                }
        }.also(::add)
    }

    val promoCardRecyclerViewAdMethods = setOf(
        "renderCard",
        "setAdapter",
        "setPromoCardAdapter",
    )
    runCatching {
        val mutableClass = PromoCardRecyclerViewSetAdapterFingerprint.classDef

        mutableClass
            .filterMethods { _, method -> method.name in promoCardRecyclerViewAdMethods }
            .forEach { method ->
                mutableClass
                    .findMutableMethodOf(method)
                    .returnEarly()
            }
    }.also(::add)

    setOf(
        OnAdLoadExecutorFingerprint,
        MyTargetManagerInitSdkFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.method.returnEarly()
        }.also(::add)
    }
}
