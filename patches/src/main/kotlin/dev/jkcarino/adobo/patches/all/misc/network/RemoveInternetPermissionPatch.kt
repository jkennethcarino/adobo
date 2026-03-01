package dev.jkcarino.adobo.patches.all.misc.network

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeUsesPermission

@Suppress("unused")
val removeInternetPermissionPatch = resourcePatch(
    name = "Remove internet permission",
    description = "Removes unnecessary internet permission from apps that " +
        "can still work without internet access.",
    use = false
) {
    execute {
        androidManifest {
            removeUsesPermission("""android\.permission\.INTERNET""")
        }
    }
}
