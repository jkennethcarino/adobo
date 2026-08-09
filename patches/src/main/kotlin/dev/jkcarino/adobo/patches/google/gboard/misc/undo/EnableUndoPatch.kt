package dev.jkcarino.adobo.patches.google.gboard.misc.undo

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.featureflags.toggleFeatureFlag
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD
import java.util.logging.Logger

@Suppress("unused")
val enableUndoPatch = bytecodePatch(
    name = "Enable Undo feature",
    description = "Enables the undo feature to quickly undo or correct typing mistakes.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    val logger = Logger.getLogger(this::class.java.name)

    execute {
        runCatching {
            // Sources:
            //   - Rboard Theme Manager
            //   - GSM Flags: https://github.com/polodarb/GMS-Flags
            toggleFeatureFlag(
                flag = "undo_access_point",
                enabled = true
            )
        }.onFailure {
            logger.info(
                "Skipping 'Enable Undo feature': already enabled by default since 17.3.3.902587967"
            )
        }
    }
}
