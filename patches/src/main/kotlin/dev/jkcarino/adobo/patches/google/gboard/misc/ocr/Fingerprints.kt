package dev.jkcarino.adobo.patches.google.gboard.misc.ocr

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object OcrAccessPointFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf(),
    filters = listOf(
        // Sources:
        //   - Nail Sadykov (X/Twitter: @Nail_Sadykov)
        //   - GMS Flags: https://github.com/polodarb/GMS-Flags
        string("enable_ocr"),
        opcode(Opcode.CONST_4, MatchAfterImmediately())
    )
)
