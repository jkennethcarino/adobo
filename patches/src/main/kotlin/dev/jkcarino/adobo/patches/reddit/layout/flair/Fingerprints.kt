package dev.jkcarino.adobo.patches.reddit.layout.flair

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

private val userFlairFieldNames = setOf(
    ", authorFlairText=",
    ", authorFlairRichText="
)

internal val commentPresentationModelToStringFingerprints =
    userFlairFieldNames.map { fieldName ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string("CommentPresentationModel(id="),
                string(fieldName),
                anyInstruction(
                    opcode(Opcode.INVOKE_VIRTUAL),
                    opcode(Opcode.IGET_OBJECT),
                    location = MatchAfterImmediately()
                ),
                opcode(Opcode.IGET_OBJECT, MatchAfterImmediately())
            )
        )
    }

internal val commentToStringFingerprints =
    userFlairFieldNames.map { fieldName ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string("Comment(commentId="),
                string(fieldName),
                anyInstruction(
                    opcode(Opcode.INVOKE_VIRTUAL),
                    opcode(Opcode.CONST_STRING),
                    location = MatchAfterImmediately()
                ),
                opcode(Opcode.IGET_OBJECT, MatchAfterImmediately())
            )
        )
    }
