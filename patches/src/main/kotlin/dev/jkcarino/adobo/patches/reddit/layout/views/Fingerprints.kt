package dev.jkcarino.adobo.patches.reddit.layout.views

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode
import dev.jkcarino.adobo.patches.reddit.shared.LinkToStringFingerprint

internal val toStringFingerprints =
    setOf(
        "ActionCellFragment(id=" to ", viewCount=",
        "MetadataCellFragment(id=" to ", viewCount=",
        "MetadataHeaderElement(linkId=" to ", viewsCount=",
    ).map { (toString, viewCount) ->
        Fingerprint(
            returnType = "Ljava/lang/String;",
            parameters = listOf(),
            filters = listOf(
                string(toString),
                string(viewCount),
                opcode(Opcode.INVOKE_VIRTUAL, MatchAfterImmediately()),
                opcode(Opcode.IGET_OBJECT, MatchAfterImmediately())
            )
        )
    }

internal object GetViewCountFingerprint : Fingerprint(
    classFingerprint = LinkToStringFingerprint,
    name = "getViewCount",
    returnType = "Ljava/lang/Long;",
    parameters = listOf()
)
