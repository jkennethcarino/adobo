package dev.jkcarino.adobo.patches.reddit.misc.sharing.url

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchFirst
import app.morphe.patcher.instanceOf
import app.morphe.patcher.methodCall

internal object CreateShareLinkFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/util/Map;",
    ),
    filters = listOf(
        methodCall(
            definingClass = $$"Landroid/net/Uri$Builder;",
            name = "clearQuery",
            returnType = $$"Landroid/net/Uri$Builder;"
        )
    )
)

internal object GetShortUrlFingerprint : Fingerprint(
    returnType = "L",
    parameters = listOf("Ljava/lang/String;", "L"),
    filters = listOf(
        instanceOf($$"RemoteGqlSharingDataSource$getShortUrl")
    )
)

internal val shareLinkFactoryGetShortUrlFingerprints =
    setOf(
        $$"/ShareLinkFactory$getShortUrlLegacy$1;",
        $$"/ShareLinkFactory$getShortUrlObserved$1;"
    ).map { className ->
        Fingerprint(
            returnType = "L",
            parameters = listOf(
                "Ljava/lang/String;",
                "Ljava/lang/String;",
                "L"
            ),
            filters = listOf(
                instanceOf(className, MatchFirst())
            )
        )
    }
