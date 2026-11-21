package dev.jkcarino.adobo.patches.reddit.layout.moderation.autoenforcebanner

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterAnywhere
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.InstructionLocation.MatchAfterWithin
import app.morphe.patcher.InstructionLocation.MatchBeforeAnywhere
import app.morphe.patcher.StringComparisonType
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.literal
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

internal object AutoEnforceBannerFingerprint : Fingerprint(
    name = "invoke",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    filters = listOf(
        anyInstruction(
            literal(0x42200000),
            opcode(Opcode.PACKED_SWITCH)
        ),
        opcode(Opcode.CHECK_CAST),
        opcode(Opcode.NEW_INSTANCE, MatchAfterWithin(1)),
        opcode(Opcode.INVOKE_DIRECT, MatchAfterWithin(1)),
        opcode(Opcode.NEW_INSTANCE, MatchAfterImmediately()),
        anyInstruction(
            string(
                string = "\n      DELETE FROM",
                comparison = StringComparisonType.STARTS_WITH,
                location = MatchAfterAnywhere()
            ),
            string(
                string = "\n      DELETE FROM",
                comparison = StringComparisonType.STARTS_WITH,
                location = MatchBeforeAnywhere()
            ),
            literal(-0x5294d540, location = MatchAfterImmediately())
        )
    )
)
