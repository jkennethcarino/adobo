package dev.jkcarino.adobo.patches.reddit.layout.bottomnav.autohide

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.fieldAccess

internal object OnBottomNavStateChangeFingerprint : Fingerprint(
    definingClass = "/BottomNavScreen;",
    returnType = "V",
    parameters = listOf("F"),
    filters = listOf(
        fieldAccess(
            definingClass = "/BottomNavScreen;",
            location = MatchFirst()
        )
    )
)
