package dev.jkcarino.adobo.patches.reddit.layout.search.ask

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

internal object TrailingAskButtonFingerprint : Fingerprint(
    accessFlags = listOf(
        AccessFlags.PUBLIC,
        AccessFlags.STATIC,
        AccessFlags.FINAL
    ),
    returnType = "V",
    filters = listOf(
        string("trailing_ask_button"),
        string("search_ask_icon"),
        string("search_ask_label"),
    )
)
