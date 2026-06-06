package dev.jkcarino.adobo.patches.reddit.layout.homeswipe

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.literal
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
