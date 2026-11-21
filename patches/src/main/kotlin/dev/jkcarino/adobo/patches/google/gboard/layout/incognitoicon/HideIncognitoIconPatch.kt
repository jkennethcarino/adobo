package dev.jkcarino.adobo.patches.google.gboard.layout.incognitoicon

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.patch
import dev.jkcarino.adobo.patches.google.gboard.detection.signature.bypassSignaturePatch
import dev.jkcarino.adobo.patches.google.gboard.shared.COMPATIBILITY_GBOARD

@Suppress("unused")
val hideIncognitoIconPatch = patch(
    name = "Hide incognito icon",
    description = "Replaces the incognito icon from the toolbar with the normal access point " +
        "icon. Incognito mode still applies to the current input session.",
    default = false
) {
    compatibleWith(COMPATIBILITY_GBOARD)

    dependsOn(bypassSignaturePatch)

    execute {
        UpdateAccessPointMenuIconFingerprint.method.addInstruction(
            index = 0,
            smaliInstructions = "const/4 p3, 0x0"
        )
    }
}
