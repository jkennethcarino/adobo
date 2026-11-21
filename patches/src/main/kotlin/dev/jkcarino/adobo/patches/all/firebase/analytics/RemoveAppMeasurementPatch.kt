package dev.jkcarino.adobo.patches.all.firebase.analytics

import app.morphe.patcher.patch.patch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.removeReceiver
import dev.jkcarino.adobo.patches.shared.resource.removeService

val removeAppMeasurementPatch = patch(
    description = "Removes App Measurement's broadcast receivers and services."
) {
    execute {
        androidManifest {
            removeReceiver("""com\.google\.android\.gms\.measurement\..+Receiver$""")
            removeService("""com\.google\.android\.gms\.measurement\..+Service$""")
        }
    }
}
