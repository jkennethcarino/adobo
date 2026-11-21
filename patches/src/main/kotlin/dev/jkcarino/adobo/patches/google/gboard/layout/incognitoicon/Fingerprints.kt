package dev.jkcarino.adobo.patches.google.gboard.layout.incognitoicon

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object CreateDefinitionFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.STATIC),
    parameters = listOf("Ljava/lang/String;", "Z"),
    filters = listOf(
        string("default"),
        string("layout"),
        string("closeAction")
    )
)

internal object UpdateAccessPointMenuIconFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Landroid/view/inputmethod/EditorInfo;", "Z", "Z"),
    filters = listOf(
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            type = { CreateDefinitionFingerprint.classDef },
            location = MatchFirst()
        ),
        fieldAccess(
            opcode = Opcode.IGET_OBJECT,
            definingClass = { CreateDefinitionFingerprint.classDef },
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.IF_EQZ, MatchAfterImmediately())
    )
)
