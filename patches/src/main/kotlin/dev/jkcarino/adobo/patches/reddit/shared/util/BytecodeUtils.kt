package dev.jkcarino.adobo.patches.reddit.shared.util

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import dev.jkcarino.adobo.util.toHexString

fun Fingerprint.overrideFieldValue(value: Any?) {
    val fieldReference = instructionMatches.last().fieldReference

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
