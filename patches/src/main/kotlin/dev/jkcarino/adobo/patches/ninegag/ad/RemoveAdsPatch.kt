package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG
import dev.jkcarino.adobo.patches.ninegag.shared.NINEGAG_AD_HOSTS
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove 9GAG's ads, trackers, and analytics",
    description = "Removes ads, trackers, and analytics in the 9GAG app."
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    dependsOn(
        baseHostsBlockerPatch {
            HostsBlockerConfig(
                hostsBlocker = HostsBlocker.fromString(NINEGAG_AD_HOSTS)
            )
        },
        hideAdContainersPatch
    )

    execute {
        AdGateFingerprint.method.returnEarly()

        RuntimeAdGateFingerprint.method.addInstructions(
            index = 0,
            smaliInstructions = """
                const/4 v0, 0x0
                invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v0
                return-object v0
            """
        )
    }
}
