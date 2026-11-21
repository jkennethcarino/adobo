package dev.jkcarino.adobo.patches.imdb.ad

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.imdb.shared.COMPATIBILITY_IMDB

@Suppress("unused")
val removeAdsPatch = patch(
    name = "Remove IMDb's ads, trackers, and analytics",
    description = "Removes ads, trackers, and analytics in the IMDb app."
) {
    compatibleWith(COMPATIBILITY_IMDB)

    dependsOn(
        baseHostsBlockerPatch {
            HostsBlockerConfig(
                hostsBlocker = HostsBlocker.fromString(AD_HOSTS)
            )
        }
    )

    execute {
        setOf(
            InflateHtmlViewFingerprint,
            HtmlViewLoadDataImplFingerprint,
            AdvertisingMultiSourceAdWidgetFingerprint,
        ).forEach { fingerprint ->
            fingerprint.method.returnEarly()
        }

        multiSourceAdWidgetFingerprints.forEach { fingerprint ->
            fingerprint.method.returnEarly()
        }

        // Applicable only to version 9.3.2 and earlier
        PageFrameworkWidgetInflateHtmlViewFingerprint
            .methodOrNull
            ?.returnEarly()
    }
}
