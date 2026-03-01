package dev.jkcarino.adobo.patches.reddit.misc.firebase

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.all.firebase.installations.baseSpoofAndroidCertPatch

val spoofCertificateHashPatch = bytecodePatch(
    description = "Spoofs the app's Firebase certificate hash to allow push notifications."
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(
        baseSpoofAndroidCertPatch(
            certificateHash = { "8bca1dda8b2418e5300fc91ac43bbf211290792e" }
        )
    )
}
