package dev.jkcarino.adobo.patches.reddit.shared.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.toHexString

fun BytecodePatchContext.updateClassField(
    classDef: ClassDef,
    fieldReference: FieldReference,
    value: Any?,
) {
    val boolValue =
        when (value) {
            is Boolean, null -> value.toHexString()
            else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass}")
        }

    val constructor = mutableClassDefBy(classDef)
        .methods
        .first { method -> method.name == "<init>" }

    val fieldIndex = constructor
        .instructions
        .indexOfFirst { instruction ->
            val currentFieldReference = instruction
                .getReference<FieldReference>()
                ?: return@indexOfFirst false

            currentFieldReference.definingClass == fieldReference.definingClass &&
                currentFieldReference.name == fieldReference.name &&
                currentFieldReference.type == fieldReference.type
        }

    val paramRegister = constructor
        .getInstruction<TwoRegisterInstruction>(fieldIndex)
        .registerA

    constructor.addInstruction(
        index = fieldIndex,
        smaliInstructions = "const/4 v$paramRegister, $boolValue"
    )
}
