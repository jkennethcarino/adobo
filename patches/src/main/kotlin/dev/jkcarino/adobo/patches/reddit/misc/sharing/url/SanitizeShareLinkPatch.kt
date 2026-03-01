package dev.jkcarino.adobo.patches.reddit.misc.sharing.url

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch

@Suppress("unused")
val sanitizeShareLinkPatch = bytecodePatch(
    name = "Sanitize share links",
    description = "Unshortens and removes the tracking query parameters from shared links.",
    use = true
) {
    dependsOn(spoofCertificateHashPatch)

    compatibleWith("com.reddit.frontpage")

    execute {
        CreateShareLinkFingerprint.method.addInstruction(
            index = 0,
            smaliInstructions = "return-object p0"
        )

        val generateShareLinkMethod = GenerateShareLinkFingerprint.method
        val getShortUrlMethod = navigate(generateShareLinkMethod)
            .to(GenerateShareLinkFingerprint.instructionMatches.last().index - 1)
            .stop()

        getShortUrlMethod.addInstruction(
            index = 0,
            smaliInstructions = "return-object p1"
        )
    }
}
