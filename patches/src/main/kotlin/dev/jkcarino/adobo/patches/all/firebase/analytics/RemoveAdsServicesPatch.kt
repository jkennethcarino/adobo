package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeProperty
import dev.jkcarino.adobo.patches.shared.resource.removeUsesPermission

val removeAdsServicesPatch = resourcePatch(
    description = "Removes AdServices config and permissions."
) {
    execute {
        androidManifest {
            removeUsesPermission(
                """android\.permission\.ACCESS_ADSERVICES_ATTRIBUTION""",
                """android\.permission\.ACCESS_ADSERVICES_AD_ID""",
            )
            removeProperty(
                """android\.adservices\.AD_SERVICES_CONFIG""",
            )
        }
    }
}
