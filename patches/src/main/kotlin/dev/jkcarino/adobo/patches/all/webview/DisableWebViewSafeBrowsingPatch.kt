package dev.jkcarino.adobo.patches.all.webview

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.metaData

@Suppress("unused")
val disableWebViewSafeBrowsingPatch = resourcePatch(
    name = "Disable Google Safe Browsing in WebView",
    description = "Disables the Google Safe Browsing checks in WebView. " +
        "This doesn't apply to WebView within the SDK Runtime.",
    use = false
) {
    execute {
        androidManifest {
            metaData("android.webkit.WebView.EnableSafeBrowsing" to "false")
        }
    }
}
