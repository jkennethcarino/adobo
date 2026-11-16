package dev.jkcarino.adobo.patches.all.screenshot.detection

import app.morphe.patcher.patch.patch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeUsesPermission

val removeDetectScreenCapturePatch = patch(
    description = "Removes the screenshot detection permission."
) {
    execute {
        androidManifest {
            removeUsesPermission("""android\.permission\.DETECT_SCREEN_CAPTURE""")
        }
    }
}
