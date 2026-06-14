package dev.jkcarino.adobo.patches.google.gboard.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.Compatibility

val COMPATIBILITY_GBOARD =
    Compatibility(
        name = "Gboard",
        packageName = "com.google.android.inputmethod.latin",
        apkFileType = ApkFileType.APK,
        signatures = setOf(
            // Android 13+
            "7ce83c1b71f3d572fed04c8d40c5cb10ff75e6d87d9df6fbd53f0468c2905053",
            // Android 7+
            "f0fd6c5b410f25cb25c3b53346c8972fae30f8ee7411df910480ad6b2d60db83",
        )
    )
