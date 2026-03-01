package dev.jkcarino.adobo.patches.google.gboard.misc.undo

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch

@Suppress("unused")
val enableUndoPatch = bytecodePatch(
    name = "Enable Undo feature",
    description = "Enables undo feature to quickly undo or correct typing mistakes.",
    use = true
) {
    compatibleWith("com.google.android.inputmethod.latin")

    dependsOn(bypassSignaturePatch)

    execute {
        UndoAccessPointFingerprint.method.apply {
            val isEnabledIndex = UndoAccessPointFingerprint.instructionMatches.last().index

            replaceInstruction(
                index = isEnabledIndex,
                smaliInstruction = "const/4 v1, 0x1"
            )
        }
    }
}
