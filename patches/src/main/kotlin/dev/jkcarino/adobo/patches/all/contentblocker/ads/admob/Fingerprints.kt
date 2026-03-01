package dev.jkcarino.adobo.patches.all.contentblocker.ads.admob

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object GoogleAdMobBaseAdViewFingerprint : Fingerprint(
    returnType = "V",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_STRING,
        Opcode.INVOKE_STATIC,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.INVOKE_STATIC,
    ),
    strings = listOf("#008 Must be called on the main UI thread."),
    custom = { _, classDef ->
        classDef.superclass == "Landroid/view/ViewGroup;"
    }
)

internal object GoogleAdMobBannerAdFingerprint : Fingerprint(
    returnType = "V",
    strings = listOf("The ad size and ad unit ID must be set before loadAd is called.")
)

internal object GoogleAdMobNativeAdFingerprint : Fingerprint(
    returnType = "V",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_STATIC,
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
    ),
    strings = listOf("Failed to load ad."),
    custom = { _, classDef ->
        classDef.superclass == "Ljava/lang/Object;"
    }
)
