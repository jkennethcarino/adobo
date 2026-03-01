package dev.jkcarino.adobo.patches.reddit.layout.screenshotbanner

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object OnScreenCapturedFingerprint : Fingerprint(
    name = "onScreenCaptured",
    returnType = "V",
    parameters = listOf(),
    custom = { _, classDef ->
        classDef.interfaces.any { it == "Landroid/app/Activity\$ScreenCaptureCallback;" }
    }
)

internal object ScreenshotBannerInvokeSuspendFingerprint : Fingerprint(
    name = "invokeSuspend",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IGET_OBJECT,
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_INTERFACE,
    ),
    custom = { _, classDef ->
        classDef.type.contains("/RedditScreenshotTriggerSharingListener\$ScreenshotBanner")
    }
)

internal object ScreenshotTakenBannerInvokeSuspendFingerprint : Fingerprint(
    name = "invokeSuspend",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Ljava/lang/Object;"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IGET_OBJECT,
        Opcode.SGET,
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_INTERFACE,
    ),
    custom = { _, classDef ->
        classDef.type.contains("/ScreenshotTakenBannerKt\$ScreenshotTakenBanner")
    }
)
