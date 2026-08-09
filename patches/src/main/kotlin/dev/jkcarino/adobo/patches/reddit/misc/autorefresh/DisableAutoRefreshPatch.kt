package dev.jkcarino.adobo.patches.reddit.misc.autorefresh

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val disableAutoRefreshPatch = bytecodePatch(
    name = "Disable home feed auto-refresh",
    description = "Disables the automatic refresh of the home feed " +
        "after viewing a post or after being away from the app.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        OnAutoRefreshFeedFingerprint.method.apply {
            OnAutoRefreshFeedFingerprint
                .instructionMatches
                .reversed()
                .forEach { match ->
                    removeInstruction(match.index)
                }
        }
    }
}
