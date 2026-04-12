package dev.jkcarino.adobo.patches.google.gboard.layout.keyshape

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal object MorePillKeysFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
    returnType = "V",
    parameters = listOf(),
    filters = listOf(
        // Sources:
        //   - AssembleDebug (Telegram: @Assembledebug)
        //   - GMS Flags: https://t.me/gmsflags_content/53
        string("more_pill_keys"),
        opcode(Opcode.INVOKE_STATIC, MatchAfterImmediately()),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately())
    )
)
