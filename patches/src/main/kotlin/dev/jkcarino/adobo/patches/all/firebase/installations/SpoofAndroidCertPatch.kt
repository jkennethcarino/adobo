package dev.jkcarino.adobo.patches.all.firebase.installations

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption

@Suppress("unused")
val spoofAndroidCertPatch = bytecodePatch(
    name = "Spoof Firebase certificate hash",
    description = "Spoofs the app's package certificate hash used by Firebase Installations " +
        "so that push notifications, remote config, and other Firebase services continue to " +
        "work as expected.",
    use = false
) {
    val certificateHash by stringOption(
        key = "certificateHash",
        title = "Certificate hash",
        description = "The SHA-1 hash of the app's package certificate.",
        required = true
    ) { hash ->
        val hexPattern = """^[0-9A-Fa-f]{40}$""".toRegex()
        !hash.isNullOrEmpty() && hash.matches(hexPattern)
    }

    dependsOn(
        baseSpoofAndroidCertPatch { certificateHash!! }
    )
}
