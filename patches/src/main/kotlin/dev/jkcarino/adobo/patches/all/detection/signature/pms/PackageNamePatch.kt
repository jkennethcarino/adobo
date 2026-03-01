package dev.jkcarino.adobo.patches.all.detection.signature.pms

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.MANIFEST_NODE
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.util.get

internal lateinit var appPackageName: String
    private set

val packageNamePatch = resourcePatch(
    description = "Extracts the package name of the app."
) {
    execute {
        androidManifest {
            val manifest = this[MANIFEST_NODE]
            appPackageName = manifest["package"]
        }
    }
}
