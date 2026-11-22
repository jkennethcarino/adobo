package dev.jkcarino.adobo.patches.reddit.layout.suspendedbanner

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.checkCast
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object CheckSuspendedReasonFingerprint : Fingerprint(
    definingClass = "/HomePagerScreenPresenter",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/Object;",
    filters = listOf(
        checkCast("/HomePagerScreen;"),
        fieldAccess(
            definingClass = "/HomePagerScreen;",
            location = MatchAfterImmediately()
        ),
        fieldAccess(
            opcode = Opcode.SGET_OBJECT,
            definingClass = "Ljava/lang/Boolean;",
            name = "TRUE",
            location = MatchAfterImmediately()
        )
    )
)

internal object OnCreateViewFingerprint : Fingerprint(
    definingClass = "/InboxTabPagerScreen;",
    returnType = "Landroid/view/View;",
    parameters = listOf(
        "Landroid/view/LayoutInflater;",
        "Landroid/view/ViewGroup;"
    ),
    filters = listOf(
        methodCall(
            name = "isSuspended",
            returnType = "Z"
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately())
    )
)

internal object ShouldShowSuspendedBannerFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Z",
    filters = listOf(
        opcode(Opcode.IF_EQZ),
        methodCall(
            name = "isSuspended",
            returnType = "Z",
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
        methodCall(
            definingClass = "Lcom/reddit/domain/model/MyAccount;",
            name = "isHumanVerificationActionNeeded",
            returnType = "Z"
        )
    )
)

internal object ShouldShowSuspendedBannerV2Fingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "Ljava/lang/Object;",
    filters = listOf(
        opcode(Opcode.IF_EQZ),
        methodCall(
            name = "isSuspended",
            returnType = "Z",
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_RESULT, MatchAfterImmediately()),
        fieldAccess(
            definingClass = "Lcom/reddit/safety/appeals/usersuspended/",
            type = "Z"
        ),
        fieldAccess(
            definingClass = "Lcom/reddit/safety/appeals/usersuspended/",
            type = "I",
            location = MatchAfterImmediately()
        )
    )
)
