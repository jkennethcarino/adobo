package dev.jkcarino.adobo.patches.reddit.layout.postswipe

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

internal val pagerAdapterFingerprint = { definingClass: String ->
    Fingerprint(
        name = "invoke",
        filters = listOf(
            methodCall(
                definingClass = "Landroid/view/ViewGroup;",
                name = "removeView"
            ),
            methodCall(
                definingClass = "Landroid/view/View;",
                name = "findViewById"
            ),
            methodCall(
                definingClass = definingClass,
                name = "setAdapter"
            )
        )
    )
}

internal object CanScrollHorizontallyFingerprint : Fingerprint(
    name = "canScrollHorizontally",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    parameters = listOf("I"),
    filters = listOf(
        methodCall(
            definingClass = "Landroid/view/View;",
            name = "isEnabled"
        )
    )
)
