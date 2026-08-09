package dev.jkcarino.adobo.util

import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.StringComparisonType
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

class ConstClassFilter internal constructor (
    val type: () -> String,
    location: InstructionLocation
) : OpcodesFilter(listOf(Opcode.CONST_CLASS), location) {

    private val typeValue: String by lazy(type)

    override fun matches(
        enclosingMethod: Method,
        instruction: Instruction
    ): Boolean {
        if (!super.matches(enclosingMethod, instruction)) {
            return false
        }

        val reference =
            (instruction as ReferenceInstruction)
                .reference as TypeReference

        return StringComparisonType.EQUALS.compare(
            targetString = reference.type,
            searchString = typeValue
        )
    }
}

fun constClass(
    type: String,
    location: InstructionLocation = InstructionLocation.MatchAfterAnywhere()
) = ConstClassFilter({ type }, location)
