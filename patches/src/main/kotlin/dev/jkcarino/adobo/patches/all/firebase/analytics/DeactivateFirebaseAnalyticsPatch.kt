package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.metaData

@Suppress("unused")
val deactivateFirebaseAnalyticsPatch = resourcePatch(
    name = "Deactivate Firebase Analytics",
    description = "Deactivates Firebase Analytics and removes its associated " +
        "broadcast receivers and services.",
    use = false
) {
    dependsOn(
        removeAdsServicesPatch,
        removeAdvertisingIdPatch,
        removeAppMeasurementPatch,
        removeGoogleAnalyticsPatch,
    )

    execute {
        androidManifest {
            metaData("firebase_analytics_collection_deactivated" to "true")
        }
    }
}
