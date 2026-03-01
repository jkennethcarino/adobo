package dev.jkcarino.adobo.patches.all.firebase.installations

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.BuilderInstruction
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import dev.jkcarino.adobo.util.getReference

fun baseSpoofAndroidCertPatch(certificateHash: () -> String) = bytecodePatch {
    execute {
        fun BuilderInstruction.isAddRequestPropertyCall(): Boolean {
            return opcode == Opcode.INVOKE_VIRTUAL &&
                getReference<MethodReference>()?.name == "addRequestProperty"
        }

        OpenHttpUrlConnectionFingerprint.method.apply {
            val certificateHash = certificateHash().uppercase()
            val xAndroidCertIndex =
                OpenHttpUrlConnectionFingerprint.stringMatches.first().index

            val addRequestPropertyInstruction = instructions
                .slice(xAndroidCertIndex until instructions.size)
                .first(BuilderInstruction::isAddRequestPropertyCall)

            val valueRegister =
                (addRequestPropertyInstruction as FiveRegisterInstruction).registerE

            addInstruction(
                index = addRequestPropertyInstruction.location.index,
                smaliInstructions = """
                    const-string v$valueRegister, "$certificateHash"
                """
            )
        }
    }
}
