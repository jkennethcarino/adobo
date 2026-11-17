package dev.jkcarino.adobo.patches.reddit.layout.moderation.autoenforcebanner

import app.morphe.patcher.patch.patch
import app.morphe.util.addInstructionsAtControlFlowLabel
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.shared.UnitFingerprint

@Suppress("unused")
val hideAutoEnforceBannerPatch = patch(
    name = "Hide mod queue's auto-enforce banner",
    description = "Hide the auto-enforce promotion banner in the mod queue.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        AutoEnforceBannerFingerprint.method.apply {
            val unit = UnitFingerprint.instructionMatches.last().fieldReference
            val bannerBranchIndex =
                AutoEnforceBannerFingerprint.instructionMatches.first().index

            addInstructionsAtControlFlowLabel(
                insertIndex = bannerBranchIndex,
                instructions = """
                    sget-object v1, ${unit.definingClass}->${unit.name}:${unit.type}
                    return-object v1
                """
            )
        }
    }
}
