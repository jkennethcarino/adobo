package dev.jkcarino.adobo.patches.reddit.layout.actions.award

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField

@Suppress("unused")
val hideAwardsPatch = bytecodePatch(
    name = "Hide awards",
    description = "Hides the awards on Reddit posts and comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        toStringFingerprints.forEach { fingerprint ->
            val isGildableIndex = fingerprint.instructionMatches.last().index
            val isGildableInstruction =
                fingerprint.method.getInstruction<TwoRegisterInstruction>(isGildableIndex)
            val isGildableFieldReference = isGildableInstruction.getReference<FieldReference>()!!

            updateClassField(
                classDef = fingerprint.classDef,
                fieldReference = isGildableFieldReference,
                value = false
            )
        }

        IsGildableFingerprint.matchAll().forEach { match ->
            match.method.instructionsOrNull ?: return@forEach
            match.method.returnEarly(false)
        }
    }
}
