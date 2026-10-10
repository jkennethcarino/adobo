package dev.jkcarino.adobo.patches.ninegag.misc.sharing

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object AppendQueryParameterFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    returnType = "V",
    parameters = listOf("Ljava/lang/String;", "Ljava/lang/String;"),
    filters = listOf(
        string("="),
        string("&")
    )
)

internal object GetShareUrlFingerprint : Fingerprint(
    filters = listOf(
        string("ref"),
        string("android", MatchAfterImmediately()),
        methodCall(
            definingClass = $$"Landroid/net/Uri$Builder;",
            name = "appendQueryParameter",
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately())
    )
)
