package dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle

import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.util.returnEarly

internal val disablePangleOption = booleanOption(
    key = "disablePangle",
    default = true,
    title = "Pangle",
    description = "Disable Banner, Interstitial, Native, Rewarded Video, and App Open ad formats."
)

internal fun applyPanglePatch() = buildList {
    runCatching {
        SdkLoadAdFactoryFingerprint.method.returnEarly(null)
    }.also(::add)

    setOf(
        LoadAdFingerprint,
        ShowFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.matchAll().forEach { match ->
                match.method.returnEarly()
            }
        }.also(::add)
    }
}
