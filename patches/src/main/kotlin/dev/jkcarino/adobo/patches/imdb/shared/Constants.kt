package dev.jkcarino.adobo.patches.imdb.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.Compatibility

val COMPATIBILITY_IMDB =
    Compatibility(
        name = "IMDb",
        packageName = "com.imdb.mobile",
        apkFileType = ApkFileType.APK,
        signatures = setOf(
            "6a87e5e44564d487b1b759befb32c8c284ed0aa8ed52a277aedcad79de28a50d",
        )
    )
