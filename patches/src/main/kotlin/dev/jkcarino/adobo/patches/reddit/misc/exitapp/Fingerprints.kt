package dev.jkcarino.adobo.patches.reddit.misc.exitapp

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.methodCall
import app.morphe.patcher.newInstance
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

/**
 * Available starting 2026.18.0 (A/B testing), and
 * applied permanently as of version 2026.32.0.
 */
internal object InvokeFingerprint : Fingerprint(
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = listOf(),
    filters = listOf(
        methodCall(returnType = "Z"),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
        opcode(Opcode.IF_NEZ, MatchAfterImmediately()),
        opcode(Opcode.GOTO, MatchAfterImmediately()),
        newInstance("RedditExitAppOnDoubleBackClickDelegate")
    )
)

internal object ShowToastFingerprint : Fingerprint(
    classFingerprint = InvokeFingerprint,
    filters = listOf(
        opcode(Opcode.MOVE),
        opcode(Opcode.IF_EQZ, MatchAfterImmediately()),
        opcode(Opcode.ADD_INT_2ADDR, MatchAfterImmediately())
    )
)
