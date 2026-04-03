package dev.jkcarino.adobo.patches.reddit.misc.outboundlink

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val openLinksDirectlyPatch = bytecodePatch(
    name = "Open external links directly",
    description = "Opens external links directly without going through out.reddit.com."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        setOf(
            GetAllowClickTrackingFingerprint,
            GetOutboundClickTrackingFingerprint,
            GetOutboundLinkFingerprint,
        ).forEach { fingerprint ->
            fingerprint.method.returnEarly()
        }
    }
}
