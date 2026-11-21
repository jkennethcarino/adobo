package dev.jkcarino.adobo.patches.ninegag.misc.sharing

import app.morphe.patcher.StringComparisonType
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.patch
import app.morphe.patcher.patch.replaceStrings
import app.morphe.patcher.string
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG

@Suppress("unused")
val sanitizeShareLinkPatch = patch(
    name = "Sanitize share links",
    description = "Removes the tracking query parameters from shared links.",
    default = true
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    execute {
        replaceStrings(
            string("?ref=android", StringComparisonType.CONTAINS) to ""
        )

        AppendQueryParameterFingerprint.method.addInstructionsWithLabels(
            index = 0,
            smaliInstructions = """
                const-string v0, "utm_"
                invoke-virtual {p1, v0}, Ljava/lang/String;->startsWith(Ljava/lang/String;)Z
                move-result v0
                if-eqz v0, :cond_keep
                return-void
                :cond_keep
                nop
            """
        )

        GetShareUrlFingerprint.matchAllOrNull()?.forEach { fingerprint ->
            fingerprint
                .instructionMatches
                .reversed()
                .forEach { instructionMatch ->
                    fingerprint.method.removeInstruction(instructionMatch.index)
                }
        }
    }
}
