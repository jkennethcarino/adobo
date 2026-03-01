package dev.jkcarino.adobo.patches.all.firebase.performance

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.metaData

@Suppress("unused")
val deactivateFirebasePerfPatch = resourcePatch(
    name = "Deactivate Firebase Performance Monitoring",
    description = "Deactivates the collection of performance data on app start up time, " +
        "network requests, and other related metrics.",
    use = false
) {
    execute {
        androidManifest {
            metaData("firebase_performance_collection_deactivated" to "true")
        }
    }
}
