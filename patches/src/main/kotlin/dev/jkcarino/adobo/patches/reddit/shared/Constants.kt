package dev.jkcarino.adobo.patches.reddit.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

val COMPATIBILITY_REDDIT =
    Compatibility(
        name = "Reddit",
        packageName = "com.reddit.frontpage",
        apkFileType = ApkFileType.APKM,
        signatures = setOf(
            "970b91143813b4c9d5f3634f672c9fcaa5621b4efaaedafd6c235cbbb869736f",
        ),
        targets = listOf(
            AppTarget(
                version = "2026.25.0",
                minSdk = 29
            ),
            AppTarget(
                version = "2026.24.0",
                minSdk = 29
            ),
            AppTarget(
                version = "2026.23.0",
                minSdk = 29
            ),
            AppTarget(
                version = "2026.22.0",
                minSdk = 29
            ),
            AppTarget(
                version = "2026.14.0",
                minSdk = 29
            )
        )
    )
