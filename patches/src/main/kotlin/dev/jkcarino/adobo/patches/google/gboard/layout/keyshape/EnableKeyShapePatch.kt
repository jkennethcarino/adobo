package dev.jkcarino.adobo.patches.google.gboard.layout.keyshape

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.featureflags.toggleFeatureFlag
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableKeyShapePatch = bytecodePatch(
    name = "Enable key shape selection",
    description = "Enables an option to select a key shape from the theme.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        // Sources:
        //   - AssembleDebug (Telegram: @Assembledebug)
        //   - GMS Flags: https://t.me/gmsflags_content/53
        toggleFeatureFlag(
            flag = "more_pill_keys",
            enabled = true
        )
    }
}
