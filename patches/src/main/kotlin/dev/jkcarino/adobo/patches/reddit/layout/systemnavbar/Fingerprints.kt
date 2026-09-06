package dev.jkcarino.adobo.patches.reddit.layout.systemnavbar

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.methodCall

internal object EdgeToEdgeSetUpFingerprint : Fingerprint(
    filters = listOf(
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "setNavigationBarColor"
        ),
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "setStatusBarContrastEnforced",
            location = MatchAfterImmediately()
        ),
        methodCall(
            definingClass = "Landroid/view/Window;",
            name = "setNavigationBarContrastEnforced"
        )
    )
)
