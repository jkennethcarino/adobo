package dev.jkcarino.adobo.patches.ninegag.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

val COMPATIBILITY_NINEGAG =
    Compatibility(
        name = "9GAG",
        packageName = "com.ninegag.android.app",
        apkFileType = ApkFileType.APK,
        signatures = setOf(
            "85581f5f4501c97a2c8bfa6f8942cfa829b0fc2d531288d6d77ce16ff30e4219",
        ),
        targets = listOf(
            AppTarget(
                version = "8.17.5",
                minSdk = 27
            ),
            AppTarget(
                version = "8.17.4",
                minSdk = 27
            )
        )
    )
