package dev.jkcarino.adobo.patches.reddit.misc.sharing.url

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val sanitizeShareLinkPatch = bytecodePatch(
    name = "Sanitize share links",
    description = "Unshortens and removes the tracking query parameters from shared links."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        CreateShareLinkFingerprint.method.addInstruction(
            index = 0,
            smaliInstructions = "return-object p0"
        )

        GetShortUrlFingerprint.method.addInstruction(
            index = 0,
            smaliInstructions = "return-object p1"
        )

        shareLinkFactoryGetShortUrlFingerprints.forEach { fingerprint ->
            fingerprint.methodOrNull?.addInstruction(
                index = 0,
                smaliInstructions = "return-object p2"
            )
        }
    }
}
