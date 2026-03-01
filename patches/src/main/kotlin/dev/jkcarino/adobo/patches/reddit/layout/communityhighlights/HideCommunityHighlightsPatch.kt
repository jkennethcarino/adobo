package dev.jkcarino.adobo.patches.reddit.layout.communityhighlights

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
import dev.jkcarino.adobo.util.getReference

@Suppress("unused")
val hideCommunityHighlightsPatch = bytecodePatch(
    name = "Hide community highlights",
    description = "Hides the community highlights section.",
    use = false
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(spoofCertificateHashPatch)

    execute {
        val toStringFingerprint = SubredditInfoByIdToStringFingerprint
        val highlightedPostsIndex = toStringFingerprint.stringMatches.last().index + 2
        val highlightedPostsInstruction =
            toStringFingerprint.method.getInstruction<TwoRegisterInstruction>(highlightedPostsIndex)
        val highlightedPostsFieldReference =
            highlightedPostsInstruction.getReference<FieldReference>()!!

        updateClassField(
            classDef = toStringFingerprint.classDef,
            fieldReference = highlightedPostsFieldReference,
            value = null
        )

        InvokeFingerprint.method.apply {
            val uiStateInterface = LoadedToStringFingerprint.classDef.interfaces.first()
            val communityHighlightsStateIndex =
                instructions.indexOfFirst { instruction ->
                    instruction.opcode == Opcode.CHECK_CAST
                        && instruction.getReference<TypeReference>()?.type == uiStateInterface
                }

            val unitFingerprint = UnitFingerprint.match(UnitToStringFingerprint.classDef)
            val unitIndex = unitFingerprint.instructionMatches.last().index
            val unitInstruction =
                unitFingerprint.method.getInstruction<OneRegisterInstruction>(unitIndex)
            val unitFieldReference = unitInstruction.getReference<FieldReference>()!!

            val unitDefiningClass = unitFieldReference.definingClass
            val unitFieldName = unitFieldReference.name
            val unitFieldType = unitFieldReference.type

            addInstructions(
                index = communityHighlightsStateIndex,
                smaliInstructions = """
                    sget-object v1, $unitDefiningClass->$unitFieldName:$unitFieldType
                    return-object v1
                """
            )
        }
    }
}
