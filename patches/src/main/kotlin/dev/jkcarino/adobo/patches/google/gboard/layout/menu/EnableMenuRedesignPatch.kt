package dev.jkcarino.adobo.patches.google.gboard.layout.menu

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.featureflags.toggleFeatureFlag
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val enableMenuRedesignPatch = bytecodePatch(
    name = "Enable access points menu redesign",
    description = "Enables the redesign of the additional actions menu.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        // Sources:
        //   - MrFaDev (Telegram: @MrFaDev)
        //   - GMS Insight: https://t.me/s/gms_insight
        toggleFeatureFlag(
            flag = "enable_access_points_menu_redesign",
            enabled = true
        )
    }
}
