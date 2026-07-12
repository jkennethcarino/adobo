package dev.jkcarino.adobo.patches.google.gboard.misc.ocr

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.featureflags.toggleFeatureFlag
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableOcrPatch = bytecodePatch(
    name = "Enable OCR feature",
    description = "Enables the Scan Text feature to extract text from images " +
        "and insert them into text fields."
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        // Sources:
        //   - Nail Sadykov (X/Twitter: @Nail_Sadykov)
        //   - GMS Flags: https://github.com/polodarb/GMS-Flags
        toggleFeatureFlag(
            flag = "enable_ocr",
            enabled = true
        )
    }
}
