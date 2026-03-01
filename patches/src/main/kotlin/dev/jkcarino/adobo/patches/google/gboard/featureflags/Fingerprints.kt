package dev.jkcarino.adobo.patches.google.gboard.featureflags

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal val featureFlagFingerprint = { flag: String ->
    Fingerprint(
        accessFlags = listOf(AccessFlags.STATIC, AccessFlags.CONSTRUCTOR),
        returnType = "V",
        parameters = listOf(),
        filters = listOf(
            string(flag),
            opcode(Opcode.CONST_4, MatchAfterImmediately())
        )
    )
}
