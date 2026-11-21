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
                version = "8.30.04",
                minSdk = 27
            ),
            AppTarget(
                version = "8.29.08",
                minSdk = 27
            ),
            AppTarget(
                version = "8.26.1",
                minSdk = 27
            ),
            AppTarget(
                version = "8.25.1",
                minSdk = 27
            ),
            AppTarget(
                version = "8.24.8",
                minSdk = 27
            )
        )
    )
