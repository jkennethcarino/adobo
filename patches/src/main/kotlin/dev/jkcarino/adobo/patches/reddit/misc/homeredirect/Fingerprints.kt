package dev.jkcarino.adobo.patches.reddit.misc.homeredirect

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.literal
import app.morphe.patcher.methodCall

internal object AppResumeRedirectFingerprint : Fingerprint(
    returnType = "V",
    parameters = listOf("L"),
    filters = listOf(
        fieldAccess(
            definingClass = "Lcom/reddit/screen/AppResumeAction;",
            name = "REDIRECT_TO_HOME"
        ),
        literal(0x10008000)
    )
)

internal object OnResumeFingerprint : Fingerprint(
    returnType = "V",
    name = "onResume",
    filters = listOf(
        methodCall(
            name = "onResume",
            location = InstructionLocation.MatchFirst()
        ),
        fieldAccess(
            definingClass = "Lcom/reddit/screen/AppResumeAction;",
            name = "REDIRECT_TO_HOME"
        ),
        literal(0x10008000)
    )
)
