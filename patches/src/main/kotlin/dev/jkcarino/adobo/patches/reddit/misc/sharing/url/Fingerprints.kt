package dev.jkcarino.adobo.patches.reddit.misc.sharing.url

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

internal object CreateShareLinkFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/util/Map;",
    ),
    filters = listOf(
        string("url"),
        string("getQueryParameterNames(...)"),
        string("toString(...)"),
    )
)

internal object GenerateShareLinkFingerprint : Fingerprint(
    returnType = "L",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.MOVE_OBJECT,
        Opcode.IGET_OBJECT,
        null,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
    ),
    strings = listOf(
        "shareTrigger",
        "shareAction",
        "permalink",
        "share_id",
    )
)
