package dev.jkcarino.adobo.patches.reddit.layout.communityhighlights

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.OpcodesFilter
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
    strings = listOf(
        "SubredditInfoById(__typename=",
        ", highlightedPostsModeratorsInfoFragment=",
    )
)

internal object InvokeFingerprint : Fingerprint(
    name = "invoke",
    strings = listOf(
        "\$this\$AnimatedContent",
        "collapse_expand_highlight",
    )
)

internal object LoadedToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    strings = listOf("Loaded(highlightedItems=")
)
