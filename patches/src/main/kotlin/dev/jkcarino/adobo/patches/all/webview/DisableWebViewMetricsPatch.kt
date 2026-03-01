package dev.jkcarino.adobo.patches.all.webview

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.shared.resource.androidManifest
import dev.jkcarino.adobo.patches.shared.resource.metaData

@Suppress("unused")
val disableWebViewMetricsPatch = resourcePatch(
    name = "Disable metrics collection in WebView",
    description = "Disables the collection of diagnostic data or usage statistics " +
        "that are sent to Google.",
    use = false
) {
    execute {
        androidManifest {
            metaData("android.webkit.WebView.MetricsOptOut" to "true")
        }
    }
}
