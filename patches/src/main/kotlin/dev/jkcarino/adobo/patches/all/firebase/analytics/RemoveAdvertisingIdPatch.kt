package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.patch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeUsesPermission

val removeAdvertisingIdPatch = patch(
    description = "Removes the Advertising ID permission."
) {
    execute {
        androidManifest {
            removeUsesPermission("""com\.google\.android\.gms\.permission\.AD_ID""")
        }
    }
}
