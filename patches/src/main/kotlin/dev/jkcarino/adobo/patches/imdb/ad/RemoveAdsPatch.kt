package dev.jkcarino.adobo.patches.imdb.ad

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.imdb.shared.COMPATIBILITY_IMDB
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove IMDb's ads, trackers, and analytics",
    description = "Removes ads, trackers, and analytics in the IMDb app."
) {
    compatibleWith(COMPATIBILITY_IMDB)

    dependsOn(
        baseHostsBlockerPatch {
            HostsBlockerConfig(
                hostsBlocker = HostsBlocker.fromString(HOSTS)
            )
        }
    )

    execute {
        InflateHtmlViewFingerprint.method.returnEarly()
        PageFrameworkWidgetInflateHtmlViewFingerprint.method.returnEarly()
        HtmlViewLoadDataImplFingerprint.method.returnEarly()
    }
}

private const val HOSTS = """
    ad.doubleclick.net
    admob-gmats.uc.r.appspot.com
    amazon-adsystem.com
    api.beta.sdklogger.publishers.advertising.a2z.com
    api.branch.io
    api.imdb.eu-west-1.prod.paets.advertising.amazon.dev
    api2.branch.io
    api3-eu.branch.io
    cdn.branch.io
    cdn.prod.metrics.imdb.a2z.com
    cdn.prod.metrics.imdb.com
    census-app.scorecardresearch.com
    googleads.g.doubleclick.net
    logs.af-south-1.amazonaws.com
    logs.ap-northeast-1.amazonaws.com
    logs.ap-northeast-2.amazonaws.com
    logs.ap-south-1.amazonaws.com
    logs.ap-southeast-1.amazonaws.com
    logs.ap-southeast-2.amazonaws.com
    logs.ca-central-1.amazonaws.com
    logs.eu-central-1.amazonaws.com
    logs.eu-north-1.amazonaws.com
    logs.eu-south-1.amazonaws.com
    logs.eu-west-2.amazonaws.com
    logs.eu-west-3.amazonaws.com
    logs.me-south-1.amazonaws.com
    logs.sa-east-1.amazonaws.com
    logs.us-east-2.amazonaws.com
    logs.us-gov-east-1.amazonaws.com
    logs.us-gov-west-1.amazonaws.com
    logs.us-west-1.amazonaws.com
    logs.us-west-2.amazonaws.com
    mobileanalytics.us-east-1.amazonaws.com
    pagead2.googlesyndication.com
    prod.cm.publishers.advertising.a2z.com
    prod.tahoe-analytics.publishers.advertising.a2z.com
    sb.scorecardresearch.com
    sdk.iad-01.braze.com
    sondheim.braze.com
    www.googleadservices.com
"""
