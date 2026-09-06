package dev.jkcarino.adobo.patches.reddit.misc.guestmode

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.checkCast
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private object PreferencesFingerprint : Fingerprint(
    name = "<init>",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf("L"),
    filters = listOf(
        anyInstruction(
            // Added in 2026.35.0
            string("com.reddit.frontpage.autologin_budget_exceeded"),
            // Found in 2026.34.0 and earlier
            string("com.reddit.frontpage.seen_introduction")
        ),
        methodCall(definingClass = "Lcom/reddit/preferences/"),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        opcode(Opcode.IPUT_OBJECT, MatchAfterImmediately()),
        opcode(Opcode.RETURN_VOID, MatchAfterImmediately())
    )
)

internal object IsIntroductionSeenFingerprint : Fingerprint(
    classFingerprint = PreferencesFingerprint,
    returnType = "Z",
    filters = listOf(
        checkCast("Ljava/lang/Boolean;"),
        methodCall(
            definingClass = "Ljava/lang/Boolean;",
            name = "booleanValue",
            returnType = "Z"
        )
    )
)
