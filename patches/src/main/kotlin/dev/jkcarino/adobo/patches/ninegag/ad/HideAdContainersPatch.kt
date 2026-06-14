package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.patch.resourcePatch
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG
import dev.jkcarino.adobo.util.filterElements
import dev.jkcarino.adobo.util.get
import dev.jkcarino.adobo.util.set

private const val AD_CONTAINER_ID = "adview_adhesion_banner_container"
private const val LAYOUT_HEIGHT_ATTR = "android:layout_height"

val hideAdContainersPatch = resourcePatch(
    description = "Removes blank ad containers from the layout."
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    execute {
        setOf(
            "res/layout/activity_home_v2.xml",
            "res/layout/activity_simple_fragment_holder.xml",
            "res/layout/activity_standalone_swipe.xml",
            "res/layout/activity_swipe_post_comment.xml",
        ).forEach { layoutPath ->
            document(layoutPath).use { document ->
                document
                    .getElementsByTagName("FrameLayout")
                    .filterElements { it["android:id"].contains(AD_CONTAINER_ID) }
                    .forEach { it[LAYOUT_HEIGHT_ATTR] = "0dp" }
            }
        }

        document("res/layout/view_aatk_native.xml").use { document ->
            val root = document.documentElement
            root["android:visibility"] = "gone"
            root[LAYOUT_HEIGHT_ATTR] = "0dp"
        }
    }
}
