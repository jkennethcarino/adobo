package dev.jkcarino.adobo.patches.google.gboard.incognito.clipboard

import app.morphe.patcher.extensions.InstructionExtensions.removeInstructions
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch

@Suppress("unused")
val enableClipboardPatch = bytecodePatch(
    name = "Enable clipboard in incognito",
    description = "Enables clipboard support in incognito mode.",
    use = true
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(bypassSignaturePatch)

    execute {
        OnPrimaryClipChangedFingerprint.method.apply {
            val patternMatch = OnPrimaryClipChangedFingerprint.instructionMatches
            val isIncognitoModeIndex = patternMatch.first().index
            val returnVoidIndex = patternMatch.last().index
            val instructionsToRemoveCount = (returnVoidIndex - isIncognitoModeIndex) + 1

            removeInstructions(
                index = isIncognitoModeIndex,
                count = instructionsToRemoveCount
            )
        }
    }
}
