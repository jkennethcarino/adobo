package dev.jkcarino.adobo.patches.reddit.layout.views

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
val hideViewCountsPatch = bytecodePatch(
    name = "Hide post view counts",
    description = "Removes the \"views\" indicator from the post metadata."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            val viewCountIndex = fingerprint.instructionMatches.last().index
            val viewCountInstruction =
                fingerprint.method.getInstruction<TwoRegisterInstruction>(viewCountIndex)
            val viewCountFieldReference = viewCountInstruction.getReference<FieldReference>()!!

            updateClassField(
                classDef = fingerprint.classDef,
                fieldReference = viewCountFieldReference,
                value = null
            )
        }

        GetViewCountFingerprint.method.returnEarly()
    }
}
