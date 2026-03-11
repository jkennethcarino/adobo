package dev.jkcarino.adobo.patches.reddit.layout.search.searchbar

import app.morphe.patcher.Fingerprint

internal object IsEnabledFingerprint : Fingerprint(
    definingClass = "/features/delegates/HomeRevampVariant;",
    name = "isEnabled",
    returnType = "Z",
    parameters = emptyList(),
)
