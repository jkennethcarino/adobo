package dev.jkcarino.adobo.patches.all.detection.signature.pms

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.ANDROID_NAME_ATTR
import dev.jkcarino.adobo.patches.shared.resource.APPLICATION_NODE
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.util.get
import dev.jkcarino.adobo.util.set

private const val EXTENSION_CLASS_NAME =
    "dev.jkcarino.extension.all.detection.signature.pms.SignatureHookApp"

val replaceSubApplicationPatch = resourcePatch(
    description = "Sets the sub-application with our SignatureHookApp."
) {
    execute {
        androidManifest {
            val application = this[APPLICATION_NODE]
            val subApplicationName = application[ANDROID_NAME_ATTR]

            if (subApplicationName.isEmpty()) {
                application[ANDROID_NAME_ATTR] = EXTENSION_CLASS_NAME
            }
        }
    }
}
