package dev.jkcarino.adobo.patches.google.gboard.featureflags

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal val booleanFlagFingerprint = { flag: String ->
    Fingerprint(
        accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
        returnType = "V",
        parameters = listOf(),
        filters = listOf(
            string(flag),
            anyInstruction(
                opcode(Opcode.CONST_4),
                opcode(Opcode.INVOKE_STATIC),
                location = MatchAfterImmediately()
            ),
            opcode(Opcode.MOVE_RESULT_OBJECT)
        )
    )
}

internal val stringFlagFingerprint = { flag: String ->
    Fingerprint(
        accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
        returnType = "V",
        parameters = listOf(),
        filters = listOf(
            string(flag),
            opcode(Opcode.CONST_STRING, MatchAfterImmediately()),
            opcode(Opcode.INVOKE_STATIC, MatchAfterImmediately()),
            opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
            opcode(Opcode.SPUT_OBJECT, MatchAfterImmediately())
        )
    )
}
