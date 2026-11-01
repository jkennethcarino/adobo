package dev.jkcarino.adobo.patches.reddit.layout.videocarousel

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.checkCast
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.AccessFlags

internal object VideoCarouselConverterFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "L",
    parameters = listOf("L", "L"),
    filters = listOf(
        checkCast("/VideoCarouselVariant;"),
        fieldAccess(
            definingClass = "/VideoCarouselVariant;",
            name = "LIST"
        ),
        fieldAccess(
            definingClass = "/VideoCarouselLayout;",
            name = "LIST"
        ),
        fieldAccess(
            definingClass = "/VideoCarouselLayout;",
            name = "PAGER"
        )
    )
)
