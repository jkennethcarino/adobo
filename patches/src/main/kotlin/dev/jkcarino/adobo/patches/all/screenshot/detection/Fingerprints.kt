package dev.jkcarino.adobo.patches.all.screenshot.detection

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

internal object RegisterScreenCaptureCallbackFingerprint : Fingerprint(
    filters = listOf(
        methodCall(
            definingClass = "Landroid/app/Activity;",
            name = "registerScreenCaptureCallback",
            parameters = listOf(
                "Ljava/util/concurrent/Executor;",
                $$"Landroid/app/Activity$ScreenCaptureCallback;"
            ),
            returnType = "V"
        )
    )
)

internal object UnregisterScreenCaptureCallbackFingerprint : Fingerprint(
    filters = listOf(
        methodCall(
            definingClass = "Landroid/app/Activity;",
            name = "unregisterScreenCaptureCallback",
            parameters = listOf($$"Landroid/app/Activity$ScreenCaptureCallback;"),
            returnType = "V"
        )
    )
)
