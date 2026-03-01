package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeReceiver
import dev.jkcarino.adobo.patches.shared.resource.removeService

val removeGoogleAnalyticsPatch = resourcePatch(
    description = "Removes Google Analytics's broadcast receivers and services."
) {
    execute {
        androidManifest {
            removeReceiver("""com\.google\.android\.gms\.analytics\..+Receiver$""")
            removeService("""com\.google\.android\.gms\.analytics\..+Service$""")
        }
    }
}
