package dev.jkcarino.adobo.patches.reddit.shared.util

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.util.getReference
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.util.toHexString

context(_: BytecodePatchContext)
fun Fingerprint.overrideFieldValue(value: Any?) {
    val fieldIndex = instructionMatches.last().index
    val fieldInstruction =
        method.getInstruction<TwoRegisterInstruction>(fieldIndex)
    val fieldReference = fieldInstruction.getReference<FieldReference>()!!

    val overridenValue =
        when (value) {
            is Boolean, null -> value.toHexString()
            else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
        }

    val constructorFingerprint =
        Fingerprint(
            definingClass = classDef.type,
            name = "<init>",
            filters = listOf(
                fieldAccess(fieldReference)
            )
        )
    val constructor = constructorFingerprint.method
    val classFieldIndex = constructorFingerprint.instructionMatches.first().index

    val paramRegister = constructor
        .getInstruction<TwoRegisterInstruction>(classFieldIndex)
        .registerA

    constructor.addInstruction(
        index = classFieldIndex,
        smaliInstructions = "const/4 v$paramRegister, $overridenValue"
    )
}
