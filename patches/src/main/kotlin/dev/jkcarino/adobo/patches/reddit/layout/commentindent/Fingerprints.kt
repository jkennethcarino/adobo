package dev.jkcarino.adobo.patches.reddit.layout.commentindent

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

internal object CommentIndentColorPatchFingerprint : Fingerprint(
    definingClass = "Ldev/jkcarino/extension/reddit/frontpage/CommentIndentColorPatch;",
    name = "<clinit>",
    filters = listOf(
        opcode(Opcode.CONST_16, MatchFirst()),
        opcode(Opcode.FILL_ARRAY_DATA)
    )
)

internal object CommentIndentAlphaInvokeFingerprint : Fingerprint(
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    filters = listOf(
        opcode(Opcode.MOVE),
        opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately()),
        opcode(Opcode.IF_EQZ, MatchAfterImmediately()),
        literal(
            literal = 0x3ecccccd,
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE, MatchAfterImmediately())
    )
)

internal object CommentIndentDrawInvokeFingerprint : Fingerprint(
    classFingerprint = CommentIndentAlphaInvokeFingerprint,
    filters = listOf(
        opcode(Opcode.CONST_4),
        opcode(Opcode.INVOKE_STATIC_RANGE, MatchAfterImmediately())
    )
)
