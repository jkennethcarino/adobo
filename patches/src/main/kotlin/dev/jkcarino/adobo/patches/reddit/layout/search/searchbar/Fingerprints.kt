package dev.jkcarino.adobo.patches.reddit.layout.search.searchbar

import app.morphe.patcher.Fingerprint

internal object IsEnabledFingerprint : Fingerprint(
    definingClass = "/HomeRevampVariant;",
    name = "isEnabled",
    returnType = "Z",
    parameters = emptyList()
)
