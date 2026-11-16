package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG

@Suppress("unused")
val removeAdsPatch = patch(
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
        val unit = UnitFingerprint.instructionMatches.last().fieldReference

        InitializeFingerprint.method.replaceInstructions(
            index = 0,
            smaliInstructions = """
                sget-object v0, ${unit.definingClass}->${unit.name}:${unit.type}
                return-object v0
            """
        )

        ShouldShowAdsFingerprint.method.returnEarly(false)
    }
}
