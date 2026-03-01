package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideShareCountPatch = bytecodePatch(
    name = "Hide share count",
    description = "Hides the share count on Reddit posts.",
    use = false
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(spoofCertificateHashPatch)

    execute {
        val shareCountMatch = ActionCellFragmentToStringFingerprint.stringMatches.last()
        val shareCountIndex = shareCountMatch.index + 2
        val shareCountInstruction = ActionCellFragmentToStringFingerprint
            .method
            .getInstruction<TwoRegisterInstruction>(shareCountIndex)

        val actionCellFragmentClassDef = ActionCellFragmentToStringFingerprint.classDef
        val shareCountFieldReference = shareCountInstruction.getReference<FieldReference>()!!

        updateClassField(
            classDef = actionCellFragmentClassDef,
            fieldReference = shareCountFieldReference,
            value = null
        )

        GetShareCountFingerprint
            .match(LinkToStringFingerprint.classDef)
            .method
            .returnEarly()
    }
}
