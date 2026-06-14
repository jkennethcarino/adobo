package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.checkCast
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object AdGateFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(),
    accessFlags = listOf(
        AccessFlags.PUBLIC,
        AccessFlags.STATIC,
        AccessFlags.FINAL
    ),
    filters = listOf(
        opcode(Opcode.IF_EQZ),
        fieldAccess(type = "Lkotlinx/coroutines/flow/MutableStateFlow;"),
        methodCall(
            definingClass = "Lkotlinx/coroutines/flow/MutableStateFlow;",
            name = "getValue",
            returnType = "Ljava/lang/Object;"
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT),
        checkCast(type = "Ljava/lang/Boolean;"),
        methodCall(
            definingClass = "Ljava/lang/Boolean;",
            name = "booleanValue",
            returnType = "Z"
        ),
        opcode(Opcode.MOVE_RESULT),
        opcode(Opcode.IF_NEZ)
    )
)

internal object RuntimeAdGateFingerprint : Fingerprint(
    name = "invokeSuspend",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    filters = listOf(
        string("adsEnabled: consentProvided="),
        string("9Ads")
    )
)
