package dev.jkcarino.adobo.patches.google.gboard.incognito.clipboard

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object OnPrimaryClipChangedFingerprint : Fingerprint(
    name = "onPrimaryClipChanged",
    returnType = "V",
    parameters = listOf(),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ,
        Opcode.RETURN_VOID,
    ),
    strings = listOf("clipboard_primary_uri", "")
)
