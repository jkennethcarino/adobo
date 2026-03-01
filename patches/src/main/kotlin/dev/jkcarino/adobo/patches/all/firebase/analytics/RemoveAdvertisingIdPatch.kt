package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeUsesPermission

val removeAdvertisingIdPatch = resourcePatch(
    description = "Removes the Advertising ID permission."
) {
    execute {
        androidManifest {
            removeUsesPermission("""com\.google\.android\.gms\.permission\.AD_ID""")
        }
    }
}
