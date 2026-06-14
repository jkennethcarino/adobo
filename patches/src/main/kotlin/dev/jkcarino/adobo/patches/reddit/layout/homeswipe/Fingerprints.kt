package dev.jkcarino.adobo.patches.reddit.layout.homeswipe

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

internal object ComposePagerScrollFingerprint : Fingerprint(
    definingClass = "Lcom/reddit/ui/compose/pager/",
    filters = listOf(
        opcode(Opcode.OR_INT_2ADDR),
        literal(
            literal = 0x6000,
            opcodes = listOf(Opcode.AND_INT_LIT16),
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_FROM16, MatchAfterImmediately())
    )
)

internal object SwipingVariantFingerprint : Fingerprint(
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    filters = listOf(
        opcode(Opcode.IF_EQZ),
        opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately()),
        fieldAccess(
            name = "INTEREST_STRIP_POPULAR_SWIPING",
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.IF_EQ, MatchAfterImmediately()),
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "setEnabled",
            location = MatchAfterImmediately()
        ),
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "setNestedScrollingEnabled",
            location = MatchAfterImmediately()
        )
    )
)
