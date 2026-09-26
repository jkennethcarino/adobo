package dev.jkcarino.adobo.patches.ninegag.misc.forcedupdate

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG

@Suppress("unused")
val disableForcedUpdatePatch = bytecodePatch(
    name = "Disable forced update dialog",
    description = "Disables the forced update dialog on an outdated app version.",
    default = false
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    execute {
        ShowCheckUpgradeDialogFingerprint.method.returnEarly()
    }
}
