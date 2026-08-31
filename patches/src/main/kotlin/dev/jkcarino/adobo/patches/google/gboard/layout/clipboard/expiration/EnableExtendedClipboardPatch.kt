package dev.jkcarino.adobo.patches.google.gboard.layout.clipboard.expiration

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.featureflags.toggleFeatureFlag
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableExtendedClipboardPatch = bytecodePatch(
    name = "Enable extended clipboard history",
    description = "Enables extending the clipboard history expiration time.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        // Sources:
        //   - Gboard Hub: https://t.me/GboardHub
        //   - GMS Insight: https://t.me/gms_insight
        toggleFeatureFlag(
            flag = "enable_extended_clipboard_history",
            enabled = true
        )
    }
}
