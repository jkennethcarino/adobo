package dev.jkcarino.adobo.patches.reddit.misc.autorefresh

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.methodCall
import app.morphe.patcher.newInstance

internal object OnAutoRefreshFeedFingerprint : Fingerprint(
    filters = listOf(
        newInstance(type = "Lcom/reddit/feeds/ui/events/OnAutoRefreshFeed;"),
        methodCall(
            definingClass = "Lcom/reddit/feeds/ui/events/OnAutoRefreshFeed;",
            name = "<init>",
            location = MatchAfterImmediately()
        ),
        methodCall(
            name = "onEvent",
            location = MatchAfterImmediately()
        )
    )
)
