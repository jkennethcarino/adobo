package dev.jkcarino.adobo.patches.reddit.layout.views

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

internal object ActionCelFragmentToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("ActionCellFragment(id="),
        string(", viewCount="),
        opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
        opcode(Opcode.IGET_OBJECT, MatchAfterImmediately())
    )
)

internal object GetViewCountFingerprint : Fingerprint(
    name = "getViewCount",
    returnType = "Ljava/lang/Long;",
    parameters = listOf()
)
