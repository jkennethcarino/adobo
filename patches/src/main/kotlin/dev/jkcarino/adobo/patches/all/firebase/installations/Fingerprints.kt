package dev.jkcarino.adobo.patches.all.firebase.installations

import app.morphe.patcher.Fingerprint

/**
 * See: https://github.com/firebase/firebase-android-sdk/blob/c8ada3ce645798bd8bacd5c9b5cb08bdf7254a34/firebase-installations/src/main/java/com/google/firebase/installations/remote/FirebaseInstallationServiceClient.java#L495
 */
internal object OpenHttpUrlConnectionFingerprint : Fingerprint(
    returnType = "Ljava/net/HttpURLConnection;",
    parameters = listOf(
        "Ljava/net/URL;",
        "Ljava/lang/String;",
    ),
    strings = listOf(
        "X-Android-Cert",
        "Firebase Installations Service is unavailable. Please try again later.",
    )
)
