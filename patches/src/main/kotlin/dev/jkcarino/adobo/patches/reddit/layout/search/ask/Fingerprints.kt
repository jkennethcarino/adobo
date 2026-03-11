package dev.jkcarino.adobo.patches.reddit.layout.search.ask

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object StaticConstructorFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    filters = listOf(
        string("isSearchBarAskButtonHoldoutEnabled"),
        string("isSearchBarAskButtonHoldoutEnabled()Z", MatchAfterImmediately()),
    )
)

internal object IsSearchBarAskButtonHoldoutEnabledFingerprint : Fingerprint(
    returnType = "Z",
    parameters = emptyList(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.SGET_OBJECT,
        Opcode.AGET_OBJECT,
        Opcode.IGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
    )
)
