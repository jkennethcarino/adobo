package dev.jkcarino.adobo.patches.all.contentblocker.ads.admob

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.StringComparisonType
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal val abstractAdViewAdapterFingerprints =
    setOf(
        "requestBannerAd",
        "requestInterstitialAd",
        "requestNativeAd",
        "showInterstitial",
    ).map { methodName ->
        Fingerprint(
            definingClass = "Lcom/google/ads/mediation/AbstractAdViewAdapter;",
            name = methodName,
            returnType = "V"
        )
    }

internal val mediationFingerprints =
    setOf(
        " does not support app open ads.",
        " does not support banner ads.",
        " does not support interstitial ads.",
        " does not support native ads.",
        " does not support rewarded ads.",
        " does not support rewarded interstitial ads.",
    ).map { string ->
        Fingerprint(
            definingClass = "Lcom/google/android/gms/ads/mediation/",
            returnType = "V",
            filters = listOf(
                string(string),
                string("com.google.android.gms.ads")
            )
        )
    }

internal object AdLoadFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "L", "L", "L"),
    filters = listOf(
        string("Context cannot be null."),
        string(
            // AppOpenAd's `load` method has "adUnitId" instead of "AdUnitId"
            string = "dUnitId cannot be null.",
            comparison = StringComparisonType.ENDS_WITH
        ),
        string("#008 Must be called on the main UI thread.")
    )
)

internal object AdLoaderLoadAdFingerprint : Fingerprint(
    returnType = "V",
    filters = listOf(
        string(
            string = "Failed to load ad",
            comparison = StringComparisonType.STARTS_WITH
        ),
        methodCall(
            definingClass = "Lcom/google/android/gms/ads/internal/util/client/",
            location = MatchAfterImmediately()
        )
    )
)

internal object AppOpenAdLoadFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/ads/appopen/AppOpenAd;",
    name = "load",
    returnType = "V"
)

internal object ShowFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/internal/ads/",
    name = "show",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;")
)

internal object BaseAdViewFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/ads/",
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

internal object BannerAdFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/ads/",
    returnType = "V",
    strings = listOf("The ad size and ad unit ID must be set before loadAd is called.")
)
