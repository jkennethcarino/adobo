package dev.jkcarino.adobo.patches.all.contentblocker.ads.bigo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object BigoAdSdkInitializeFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "V",
    strings = listOf(
        "Bigo Ads SDK init had been invoked.",
        "Bigo Ads SDK wait to initing due to empty config.",
        "Avoid initializing Bigo Ads SDK repeatedly.",
    )
)

internal object AbstractAdLoaderLoadAdFingerprint : Fingerprint(
    definingClass = "Lsg/bigo/ads/controller/loader/AbstractAdLoader;",
    name = "loadAd",
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_DIRECT,
        Opcode.MOVE_RESULT,
        Opcode.IF_NEZ,
        Opcode.INVOKE_DIRECT,
    )
)

internal object SplashAdFingerprint : Fingerprint(
    definingClass = "Lsg/bigo/ads/ad/splash/",
    strings = listOf(
        "splash_duration",
        "splash_close",
    )
)
