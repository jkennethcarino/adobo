package dev.jkcarino.adobo.patches.reddit.layout.communityhighlights

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.getReference
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.overrideFieldValue
import dev.jkcarino.adobo.patches.shared.UnitFingerprint

@Suppress("unused")
val hideCommunityHighlightsPatch = patch(
    name = "Hide community highlights",
    description = "Hides the community highlights section.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        SubredditInfoByIdToStringFingerprint.overrideFieldValue(null)

        InvokeFingerprint.method.apply {
            val uiStateInterface = LoadedToStringFingerprint.classDef.interfaces.first()
            val communityHighlightsStateIndex =
                instructions.indexOfFirst { instruction ->
                    instruction.opcode == Opcode.CHECK_CAST
                        && instruction.getReference<TypeReference>()?.type == uiStateInterface
                }
            val unit = UnitFingerprint.instructionMatches.last().fieldReference

            addInstructions(
                index = communityHighlightsStateIndex,
                smaliInstructions = """
                    sget-object v1, ${unit.definingClass}->${unit.name}:${unit.type}
                    return-object v1
                """
            )
        }
    }
}
