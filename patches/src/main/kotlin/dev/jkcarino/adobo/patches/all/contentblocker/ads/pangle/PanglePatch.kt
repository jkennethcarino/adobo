package dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle

import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.booleanOption
import dev.jkcarino.adobo.util.returnEarly

internal val disablePangleOption = booleanOption(
    key = "disablePangle",
    default = true,
    title = "Pangle",
    description = "Disable Banner, Interstitial, Native, Rewarded Video, and App Open ad formats."
)

internal fun BytecodePatchContext.applyPanglePatch() = buildList {
    runCatching {
        SdkLoadAdFactoryFingerprint.method.returnEarly()
    }.also(::add)
}
