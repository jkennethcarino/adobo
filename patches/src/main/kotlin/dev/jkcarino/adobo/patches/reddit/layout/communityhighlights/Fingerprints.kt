package dev.jkcarino.adobo.patches.reddit.layout.communityhighlights

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

private object UnitToStringFingerprint : Fingerprint(
    name = "toString",
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("kotlin.Unit", MatchFirst()),
        opcode(Opcode.RETURN_OBJECT, MatchAfterImmediately())
    )
)

internal object UnitFingerprint : Fingerprint(
    classFingerprint = UnitToStringFingerprint,
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.NEW_INSTANCE,
        Opcode.INVOKE_DIRECT,
        Opcode.SPUT_OBJECT,
    )
)

internal object SubredditInfoByIdToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("SubredditInfoById(__typename="),
        string(", highlightedPostsModeratorsInfoFragment="),
        fieldAccess(type = "L")
    )
)

internal object InvokeFingerprint : Fingerprint(
    name = "invoke",
    filters = listOf(
        string("collapse_expand_highlight"),
        string("content caret")
    )
)

internal object LoadedToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("Loaded(highlightedItems=")
    )
)
