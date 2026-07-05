package dev.jkcarino.adobo.patches.all.google.maps

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import dev.jkcarino.adobo.patches.shared.resource.ANDROID_NAME_ATTR
import dev.jkcarino.adobo.patches.shared.resource.ANDROID_VALUE_ATTR
import dev.jkcarino.adobo.patches.shared.resource.APPLICATION_NODE
import dev.jkcarino.adobo.patches.shared.resource.META_DATA_TAG
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.util.asElementSequence
import dev.jkcarino.adobo.util.get

@Suppress("unused")
val replaceMapsApiKeyPatch = resourcePatch(
    name = "Replace Google Maps API key",
    description = "Replaces the Google Maps API key with a custom key.",
    default = false
) {
    val apiKey by stringOption(
        key = "apiKey",
        title = "Maps API Key",
        description =
            "This key must be a valid Google Maps API key starting with \"AIzaSy\". \n\n" +
                "Follow the instructions at " +
                "https://developers.google.com/maps/documentation/android-sdk/get-api-key " +
                "to generate a new API key if you don't have one. \n\n" +
                "Leave it unrestricted to use it with any app (recommended), " +
                "or restrict it by adding the SHA-1 fingerprint of the app's signing " +
                "certificate and the package name.",
        required = true
    ) { key ->
        !key.isNullOrEmpty() && key.startsWith("AIzaSy")
    }

    execute {
        androidManifest {
            val application = this[APPLICATION_NODE]

            val mapsMetadata = application
                .getElementsByTagName(META_DATA_TAG)
                .asElementSequence()
                .firstOrNull { metadata ->
                    val metadataName = metadata[ANDROID_NAME_ATTR]
                    metadataName == "com.google.android.geo.API_KEY" ||
                        metadataName == "com.google.android.maps.v2.API_KEY"
                }

            mapsMetadata
                ?.setAttribute(ANDROID_VALUE_ATTR, apiKey!!)
                ?: throw PatchException(
                    "This app doesn't use Google Maps. No geo or Maps V2 API key meta-data present."
                )
        }
    }
}
