package dev.jkcarino.adobo.patches.reddit.layout.actions.share

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideShareCountPatch = bytecodePatch(
    name = "Hide share count",
    description = "Hides the share count on Reddit posts.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            val shareCountIndex = fingerprint.instructionMatches.last().index
            val shareCountInstruction =
                fingerprint.method.getInstruction<TwoRegisterInstruction>(shareCountIndex)

            val actionCellFragmentClassDef = fingerprint.classDef
            val shareCountFieldReference = shareCountInstruction.getReference<FieldReference>()!!

            updateClassField(
                classDef = actionCellFragmentClassDef,
                fieldReference = shareCountFieldReference,
                value = null
            )
        }

        GetShareCountFingerprint.method.returnEarly()
    }
}
