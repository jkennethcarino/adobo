package dev.jkcarino.adobo.patches.reddit.layout.search.ask

import app.morphe.patcher.patch.bytecodePatch
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideSearchAskButtonPatch = bytecodePatch(
    name = "Hide Ask button from search bar",
    description = "Hides the Ask button (Reddit Answers) from the search bar.",
    use = false
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(spoofCertificateHashPatch)

    execute {
        IsSearchBarAskButtonHoldoutEnabledFingerprint
            .match(StaticConstructorFingerprint.classDef)
            .method
            .returnEarly()
    }
}
