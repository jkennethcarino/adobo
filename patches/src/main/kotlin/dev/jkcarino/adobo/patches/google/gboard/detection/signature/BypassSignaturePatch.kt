package dev.jkcarino.adobo.patches.google.gboard.detection.signature

import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.returnEarly
import dev.jkcarino.adobo.patches.google.gboard.fixes.applyWorkaroundPatch
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

val bypassSignaturePatch = patch(
    description = "Bypasses the signature verification checks."
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(applyWorkaroundPatch)

    execute {
        CheckSignatureFingerprint.method.returnEarly(true)
    }
}
