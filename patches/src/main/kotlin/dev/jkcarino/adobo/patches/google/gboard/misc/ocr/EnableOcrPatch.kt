package dev.jkcarino.adobo.patches.google.gboard.misc.ocr

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch

@Suppress("unused")
val enableOcrPatch = bytecodePatch(
    name = "Enable OCR feature",
    description = "Enables OCR feature to extract text from images and insert it into text fields.",
    use = true
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(bypassSignaturePatch)

    execute {
        OcrAccessPointFingerprint.method.apply {
            val isEnabledIndex = OcrAccessPointFingerprint.instructionMatches.last().index

            replaceInstruction(
                index = isEnabledIndex,
                smaliInstruction = "const/4 v1, 0x1"
            )
        }
    }
}
