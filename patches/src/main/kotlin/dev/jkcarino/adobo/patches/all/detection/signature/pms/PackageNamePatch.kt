package dev.jkcarino.adobo.patches.all.detection.signature.pms

import app.morphe.patcher.patch.patch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.util.get

internal lateinit var appPackageName: String
    private set

val packageNamePatch = patch(
    description = "Extracts the package name of the app."
) {
    execute {
        androidManifest {
            appPackageName = root["package"]
        }
    }
}
