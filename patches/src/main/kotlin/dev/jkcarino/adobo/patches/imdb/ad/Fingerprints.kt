package dev.jkcarino.adobo.patches.imdb.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string

internal object InflateHtmlViewFingerprint : Fingerprint(
    definingClass = "/HtmlCardView;",
    returnType = "V",
    parameters = listOf("L", "L", "Ljava/lang/String;"),
    filters = listOf(
        string("HtmlCardView.inflateHtmlView"),
        string("getContext(...)"),
        string("getResources(...)")
    )
)

internal object PageFrameworkWidgetInflateHtmlViewFingerprint : Fingerprint(
    definingClass = "/PageFrameworkWidgetHtmlCardView;",
    returnType = "V",
    parameters = listOf("L", "L", "Ljava/lang/String;"),
    filters = listOf(
        string("HtmlCardView.inflateHtmlView"),
        string("getContext(...)"),
        string("getResources(...)")
    )
)

internal object HtmlViewLoadDataImplFingerprint : Fingerprint(
    definingClass = "/HtmlView;",
    returnType = "V",
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "L",
        "Z"
    ),
    filters = listOf(
        string("text/html"),
        methodCall(
            definingClass = "Landroid/webkit/WebView;",
            name = "loadDataWithBaseURL"
        )
    )
)
