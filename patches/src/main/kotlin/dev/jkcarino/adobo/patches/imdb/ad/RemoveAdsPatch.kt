package dev.jkcarino.adobo.patches.imdb.ad

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.imdb.shared.COMPATIBILITY_IMDB

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
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
        InflateHtmlViewFingerprint.method.returnEarly()
        PageFrameworkWidgetInflateHtmlViewFingerprint.method.returnEarly()
        HtmlViewLoadDataImplFingerprint.method.returnEarly()
    }
}
