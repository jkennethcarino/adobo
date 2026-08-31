package dev.jkcarino.adobo.patches.reddit.layout.screenshotbanner

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

internal object OnScreenCapturedFingerprint : Fingerprint(
    name = "onScreenCaptured",
    returnType = "V",
    parameters = listOf(),
    custom = { _, classDef ->
        classDef.interfaces.any { it == $$"Landroid/app/Activity$ScreenCaptureCallback;" }
    }
)

internal val screenshotBannerFingerprints =
    setOf(
        $$"/RedditScreenshotTriggerSharingListener$ScreenshotBanner",
        $$"/ScreenshotTakenBannerKt$ScreenshotTakenBanner",
    ).map { definingClass ->
        Fingerprint(
            definingClass = definingClass,
            name = "invokeSuspend",
            returnType = "Ljava/lang/Object;",
            parameters = listOf("Ljava/lang/Object;"),
            filters = listOf(
                opcode(Opcode.IGET_OBJECT),
                fieldAccess(
                    opcode = Opcode.SGET_OBJECT,
                    definingClass = "Ljava/lang/Boolean;",
                    name = "TRUE",
                ),
                methodCall(
                    name = "setValue",
                    location = MatchAfterImmediately()
                )
            )
        )
    }
