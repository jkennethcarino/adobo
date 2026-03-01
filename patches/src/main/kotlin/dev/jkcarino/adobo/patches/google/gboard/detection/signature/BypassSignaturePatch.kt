package dev.jkcarino.adobo.patches.google.gboard.detection.signature

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.fixes.applyWorkaroundPatch
import dev.jkcarino.adobo.util.returnEarly

val bypassSignaturePatch = bytecodePatch(
    description = "Bypasses the signature verification checks."
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(applyWorkaroundPatch)

    execute {
        CheckSignatureFingerprint.method.returnEarly(true)
    }
}
