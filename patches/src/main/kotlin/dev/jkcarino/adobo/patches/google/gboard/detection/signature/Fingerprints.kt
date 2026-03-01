package dev.jkcarino.adobo.patches.google.gboard.detection.signature

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object CheckSignatureFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC),
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/String;"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_4,
        Opcode.NEW_ARRAY,
        // 1st byte array
        Opcode.SGET_OBJECT,
        Opcode.CONST_4,
        Opcode.APUT_OBJECT,
        // 2nd byte array
        Opcode.SGET_OBJECT,
        Opcode.CONST_4,
        Opcode.APUT_OBJECT,
        // 3rd byte array
        Opcode.SGET_OBJECT,
        Opcode.CONST_4,
        Opcode.APUT_OBJECT,
        // Invokes a static method to calculate the
        // SHA-256 digest of the app package's signature
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT_OBJECT,
    )
)
