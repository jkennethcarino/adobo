package dev.jkcarino.adobo.patches.reddit.misc.outboundlink

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val openLinksDirectlyPatch = bytecodePatch(
    name = "Open external links directly",
    description = "Opens external links directly without going through out.reddit.com.",
    use = true
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(spoofCertificateHashPatch)

    execute {
        mapOf(
            AccountPreferencesToStringFingerprint to GetAllowClickTrackingFingerprint,
            AccountToStringFingerprint to GetOutboundClickTrackingFingerprint,
            LinkToStringFingerprint to GetOutboundLinkFingerprint,
        ).forEach { (toStringFingerprint, targetFingerprint) ->
            targetFingerprint
                .match(toStringFingerprint.classDef)
                .method
                .returnEarly()
        }
    }
}
