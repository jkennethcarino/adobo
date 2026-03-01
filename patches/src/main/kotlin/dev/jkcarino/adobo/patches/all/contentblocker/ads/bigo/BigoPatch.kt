package dev.jkcarino.adobo.patches.all.contentblocker.ads.bigo

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.booleanOption
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.returnEarly

internal val disableBigoOption = booleanOption(
    key = "disableBigo",
    default = true,
    title = "BIGO",
    description = "Disable Banner, Native, Interstitial, Pop-up, Rewarded, and Splash ad formats."
)

internal fun BytecodePatchContext.applyBigoPatch() = buildList {
    val adMethods = setOf(
        "show",
        "showInAdContainer",
    )
    runCatching {
        val mutableClass = SplashAdFingerprint.classDef

        mutableClass
            .filterMethods { _, method -> method.name in adMethods }
            .forEach { method ->
                mutableClass
                    .findMutableMethodOf(method)
                    .returnEarly()
            }
    }.also(::add)

    setOf(
        BigoAdSdkInitializeFingerprint,
        AbstractAdLoaderLoadAdFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.method.returnEarly()
        }.also(::add)
    }
}
