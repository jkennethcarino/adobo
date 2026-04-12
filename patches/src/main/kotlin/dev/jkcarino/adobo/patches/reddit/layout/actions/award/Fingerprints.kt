package dev.jkcarino.adobo.patches.reddit.layout.actions.award

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

internal val toStringFingerprints =
    setOf(
        "PostActionScoreBarElement(linkId=" to ", showAwardEntryPoint=",
        "PostContentFragment(__typename=" to ", isGildable=",
    ).map { (dataClass, isGildable) ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string(dataClass),
                string(isGildable),
                opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
                opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately())
            )
        )
    }.plus(CommentPresentationModelToStringFingerprint)

private object CommentPresentationModelToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("CommentPresentationModel(id="),
        string(", isGildable="),
        opcode(Opcode.CONST_STRING, MatchAfterImmediately()),
        opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately()),
        opcode(Opcode.IGET_BOOLEAN, MatchAfterImmediately())
    )
)
