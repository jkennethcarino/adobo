package dev.jkcarino.adobo.patches.reddit.layout.postswipe

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object DisablePostDetailSwipeFingerprint : Fingerprint(
    definingClass = "Ldev/jkcarino/extension/reddit/frontpage/DisablePostDetailSwipePatch;",
    name = "apply"
)

internal object PostDetailPagerFingerprint : Fingerprint(
    definingClass = "/PostDetailPagerScreen;",
    returnType = "V",
    parameters = listOf("Landroid/view/View;"),
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    filters = listOf(
        opcode(Opcode.INVOKE_SUPER),
        opcode(Opcode.IGET_OBJECT)
    )
)

internal object UpdateLayoutSuppressionFingerprint : Fingerprint(
    definingClass = "/PostDetailPagerScreen;",
    returnType = "V",
    parameters = listOf(),
    filters = listOf(
        fieldAccess(
            type = "Lcom/reddit/screen/widget/ScreenPager;"
        )
    )
)

internal object ScreenPagerOnInterceptTouchEventFingerprint : Fingerprint(
    definingClass = "Lcom/reddit/screen/widget/ScreenPager;",
    name = "onInterceptTouchEvent",
    returnType = "Z",
    parameters = listOf("Landroid/view/MotionEvent;")
)

internal object ScreenPagerOnTouchEventFingerprint : Fingerprint(
    definingClass = "Lcom/reddit/screen/widget/ScreenPager;",
    name = "onTouchEvent",
    returnType = "Z",
    parameters = listOf("Landroid/view/MotionEvent;")
)
