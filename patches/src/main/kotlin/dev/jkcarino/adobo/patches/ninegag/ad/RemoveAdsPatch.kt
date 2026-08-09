package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnBoxedBooleanEarly
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove 9GAG's ads, trackers, and analytics",
    description = "Removes ads, trackers, and analytics in the 9GAG app."
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    dependsOn(
        baseHostsBlockerPatch {
            HostsBlockerConfig(
                hostsBlocker = HostsBlocker.fromString(AD_HOSTS)
            )
        },
        hideAdContainersPatch
    )

    execute {
        AdGateFingerprint.method.returnEarly(false)
        RuntimeAdGateFingerprint.method.returnBoxedBooleanEarly(false)
    }
}
