package dev.jkcarino.adobo.patches.google.gboard.incognito.voicetyping

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import com.android.tools.smali.dexlib2.Opcode

internal object EnableVoiceTypingFingerprint : Fingerprint(
    returnType = "Z",
    parameters = listOf(
        "Landroid/content/Context;",
        "Landroid/view/inputmethod/EditorInfo;",
        "Z",
    ),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.RETURN,
        Opcode.INVOKE_STATIC,
        Opcode.MOVE_RESULT,
        Opcode.IF_NEZ,
        Opcode.IF_EQZ,
    )
)
