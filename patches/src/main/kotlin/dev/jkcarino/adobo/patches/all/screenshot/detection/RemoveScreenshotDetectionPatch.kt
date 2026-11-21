package dev.jkcarino.adobo.patches.all.screenshot.detection

import app.morphe.patcher.extensions.InstructionExtensions.removeInstruction
import app.morphe.patcher.patch.patch

@Suppress("unused")
val removeScreenshotDetectionPatch = patch(
    name = "Remove screenshot detection",
    description = "Prevents the app from detecting when a screenshot is taken.",
    default = false
) {
    dependsOn(removeDetectScreenCapturePatch)

    execute {
        RegisterScreenCaptureCallbackFingerprint.matchAll().forEach { match ->
            val registerScreenCaptureCallbackIndex = match.instructionMatches.first().index

            match.method.removeInstruction(
                index = registerScreenCaptureCallbackIndex
            )
        }

        UnregisterScreenCaptureCallbackFingerprint.matchAll().forEach { match ->
            val unregisterScreenCaptureCallbackIndex = match.instructionMatches.first().index

            match.method.removeInstruction(
                index = unregisterScreenCaptureCallbackIndex
            )
        }
    }
}
