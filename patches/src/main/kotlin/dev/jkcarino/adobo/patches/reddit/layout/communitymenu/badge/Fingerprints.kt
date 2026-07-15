package dev.jkcarino.adobo.patches.reddit.layout.communitymenu.badge

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.Opcode

internal object CommunityMenuBadgeFingerprint : Fingerprint(
    filters = listOf(
        opcode(Opcode.IF_EQZ),
        fieldAccess(
            definingClass = "/BadgeSentiment;",
            name = "Brand",
            location = MatchAfterImmediately()
        ),
        string("community_menu_badge", MatchAfterImmediately())
    )
)
