package dev.jkcarino.adobo.patches.all.contentblocker.ads

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.admob.applyGoogleAdMobPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.admob.disableGoogleAdMobOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.applovin.applyAppLovinMaxPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.applovin.disableAppLovinMaxOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.bigo.applyBigoPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.bigo.disableBigoOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.meta.applyMetaAudienceNetworkPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.meta.disableMetaAudienceNetworkOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.mintegral.applyMintegralPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.mintegral.disableMintegralOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.mytarget.applyMyTargetPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.mytarget.disableMyTargetOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle.applyPanglePatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.pangle.disablePangleOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.topon.applyTopOnPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.topon.disableTopOnOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.unity.applyUnityPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.unity.disableUnityOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.vungle.applyVunglePatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.vungle.disableVungleOption
import dev.jkcarino.adobo.patches.all.contentblocker.ads.yandex.applyYandexPatch
import dev.jkcarino.adobo.patches.all.contentblocker.ads.yandex.disableYandexOption
import java.util.logging.Logger

@Suppress("unused")
val disableMobileAdsPatch = bytecodePatch(
    name = "Disable mobile ads",
    description = "Disables banner, interstitial, and other ad formats.",
    use = false
) {
    val logger = Logger.getLogger(this::class.java.name)

    val appLovinMaxOption = disableAppLovinMaxOption()
    val bigoOption = disableBigoOption()
    val googleAdMobOption = disableGoogleAdMobOption()
    val metaAudienceNetworkOption = disableMetaAudienceNetworkOption()
    val mintegralOption = disableMintegralOption()
    val myTargetOption = disableMyTargetOption()
    val pangleOption = disablePangleOption()
    val topOnOption = disableTopOnOption()
    val unityOption = disableUnityOption()
    val vungleOption = disableVungleOption()
    val yandexOption = disableYandexOption()

    execute {
        val options = mapOf(
            appLovinMaxOption to ::applyAppLovinMaxPatch,
            bigoOption to ::applyBigoPatch,
            googleAdMobOption to ::applyGoogleAdMobPatch,
            metaAudienceNetworkOption to ::applyMetaAudienceNetworkPatch,
            mintegralOption to ::applyMintegralPatch,
            myTargetOption to ::applyMyTargetPatch,
            pangleOption to ::applyPanglePatch,
            topOnOption to ::applyTopOnPatch,
            unityOption to ::applyUnityPatch,
            vungleOption to ::applyVunglePatch,
            yandexOption to ::applyYandexPatch,
        )

        options.forEach { (option, patch) ->
            val isEnabled by option
            if (!isEnabled!!) {
                return@forEach
            }

            // TODO: Update once the Patcher API supports passing the 'name'
            val adNetwork = option.title

            val appliedPatch = patch()
            val total = appliedPatch.size
            val foundCount = appliedPatch.count { it.isSuccess }

            val message = when {
                foundCount == total -> "[Found] $adNetwork disabled."
                foundCount > 0 -> "[Found] $adNetwork partially disabled."
                else -> "[Skipped] $adNetwork was not found."
            }

            logger.info(message)
        }
    }
}
