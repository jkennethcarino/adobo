package dev.jkcarino.adobo.patches.google.gboard.fixes

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.util.get

val applyWorkaroundPatch = resourcePatch(
    description = "Applies workaround for patcher to fix missing or unsupported resources. " +
        "This only applies to versions 14.1.x.x and later."
) {
    compatibleWith("com.google.android.inputmethod.latin")

    execute {
        document("res/xml/method.xml").use { document ->
            val inputMethod = document["input-method"]

            // A new input method was introduced in Android 15 Developer Preview 2. However, the
            // current AAPT is unable to find this new attribute and is throwing an error.
            //
            // error: attribute android:supportsConnectionlessStylusHandwriting not found.
            // WARNING: error: failed linking file resources
            //
            // https://developer.android.com/sdk/api_diff/v-dp2-incr/changes/android.R.attr
            inputMethod.removeAttribute("android:supportsConnectionlessStylusHandwriting")
        }
    }
}
