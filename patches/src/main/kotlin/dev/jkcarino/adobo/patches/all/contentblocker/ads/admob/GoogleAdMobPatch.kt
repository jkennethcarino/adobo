package dev.jkcarino.adobo.patches.all.contentblocker.ads.admob

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.booleanOption
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.returnEarly

internal val disableGoogleAdMobOption = booleanOption(
    key = "disableGoogleAdMob",
    default = true,
    title = "Google AdMob",
    description = "Disable Banner, Interstitial, Native, Rewarded, Rewarded Interstitial, and " +
        "App Open ad formats."
)

internal fun BytecodePatchContext.applyGoogleAdMobPatch() = buildList {
    runCatching {
        // Common strings present in AppOpenAd, InterstitialAd, RewardedAd,
        // and RewardedInterstitialAd's load method
        val preconditions = setOf(
            "Context cannot be null.",
            "AdUnitId cannot be null.",
            "#008 Must be called on the main UI thread.",
        )

        classDefForEach { classDef ->
            classDef
                .filterMethods { _, method ->
                    if (
                        method.returnType != "V" ||
                        method.parameters.isEmpty() ||
                        method.parameters.first().type != "Landroid/content/Context;"
                    ) {
                        return@filterMethods false
                    }

                    val instructions = method.instructionsOrNull
                        ?: return@filterMethods false

                    val strings = preconditions.toMutableList()

                    instructions.forEach instructions@{ instruction ->
                        val string = instruction.getReference<StringReference>()
                            ?.string
                            ?: return@instructions

                        val index = strings.indexOfFirst {
                            string.contains(
                                other = it,
                                // The AppOpenAd's load method has "adUnitId" instead of "AdUnitId"
                                ignoreCase = true
                            )
                        }
                        if (index == -1) return@instructions

                        // Found a match
                        strings.removeAt(index)
                    }

                    return@filterMethods strings.isEmpty()
                }
                .ifEmpty { throw PatchException("No load ad method found") }
                .forEach { method ->
                    mutableClassDefBy(method.definingClass)
                        .findMutableMethodOf(method)
                        .returnEarly()
                }
        }
    }.also(::add)

    runCatching {
        val adMethods = setOf(
            "requestBannerAd",
            "requestInterstitialAd",
            "requestNativeAd",
            "showInterstitial",
        )
        val mutableClass =
            mutableClassDefBy("Lcom/google/ads/mediation/AbstractAdViewAdapter;")

        mutableClass
            .filterMethods { _, method -> method.name in adMethods }
            .forEach { method ->
                mutableClass
                    .findMutableMethodOf(method)
                    .returnEarly()
            }
    }.also(::add)

    setOf(
        GoogleAdMobBaseAdViewFingerprint,
        GoogleAdMobBannerAdFingerprint,
        GoogleAdMobNativeAdFingerprint,
    ).forEach { fingerprint ->
        runCatching {
            fingerprint.method.returnEarly()
        }.also(::add)
    }
}
