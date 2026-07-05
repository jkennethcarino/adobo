package dev.jkcarino.adobo.patches.google.gboard.detection.signature

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import dev.jkcarino.adobo.patches.google.gboard.fixes.applyWorkaroundPatch
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

val bypassSignaturePatch = bytecodePatch(
    description = "Bypasses the signature verification checks."
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(applyWorkaroundPatch)

    execute {
        CheckSignatureFingerprint.method.returnEarly(true)
    }
}
