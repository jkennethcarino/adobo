package dev.jkcarino.adobo.patches.google.gboard.misc.undo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object UndoAccessPointFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf(),
    filters = listOf(
        // Sources:
        //   - Rboard Theme Manager
        //   - GSM Flags: https://github.com/polodarb/GMS-Flags
        string("undo_access_point"),
        opcode(Opcode.CONST_4, MatchAfterImmediately())
    )
)
