package dev.jkcarino.adobo.patches.shared.resource

import app.morphe.patcher.patch.ResourcePatchContext
import dev.jkcarino.adobo.util.asElementSequence
import dev.jkcarino.adobo.util.createElement
import dev.jkcarino.adobo.util.filterElements
import dev.jkcarino.adobo.util.get
import dev.jkcarino.adobo.util.removeElements
import dev.jkcarino.adobo.util.set
import org.w3c.dom.Document

typealias Manifest = Document

internal const val MANIFEST_NODE = "manifest"
internal const val APPLICATION_NODE = "application"
internal const val META_DATA_TAG = "meta-data"
internal const val ANDROID_NAME_ATTR = "android:name"
internal const val ANDROID_VALUE_ATTR = "android:value"

/**
 * Applies a configuration block to the AndroidManifest document.
 */
fun ResourcePatchContext.androidManifest(
    block: Manifest.() -> Unit,
): Document = document("AndroidManifest.xml").use { document ->
    document.apply(block)
}

/**
 * Removes elements from the manifest document based on the specified tag name and element names.
 */
private fun Document.removeManifestElements(
    tagName: String,
    vararg elements: String,
    isRootLevel: Boolean = false,
) {
    val nodeName = if (isRootLevel) MANIFEST_NODE else APPLICATION_NODE
    val node = this[nodeName]
    val regexes = elements.map(String::toRegex)

    node.getElementsByTagName(tagName)
        .filterElements { element ->
            val androidName = element[ANDROID_NAME_ATTR]
            regexes.any { it.matches(androidName) }
        }
        .let(node::removeElements)
}

/**
 * Adds or updates meta-data elements in the manifest document.
 */
fun Document.metaData(vararg properties: Pair<String, String>) {
    val application = this[APPLICATION_NODE]

    properties.forEach { (name, value) ->
        val metaData = application.getElementsByTagName(META_DATA_TAG)
            .asElementSequence()
            .firstOrNull { it[ANDROID_NAME_ATTR] == name }

        if (metaData != null) {
            metaData.setAttribute(ANDROID_VALUE_ATTR, value)
        } else {
            application.appendChild(
                createElement(META_DATA_TAG) {
                    this[ANDROID_NAME_ATTR] = name
                    this[ANDROID_VALUE_ATTR] = value
                }
            )
        }
    }
}

/**
 * Removes specified [receivers] from the <application> element.
 */
fun Manifest.removeReceiver(vararg receivers: String) =
    removeManifestElements("receiver", *receivers)

/**
 * Removes specified [services] from the <application> element.
 */
fun Manifest.removeService(vararg services: String) =
    removeManifestElements("service", *services)

/**
 * Removes specified [permissions] from the <manifest> element.
 */
fun Manifest.removeUsesPermission(vararg permissions: String) =
    removeManifestElements("uses-permission", *permissions, isRootLevel = true)

/**
 * Removes specified [properties] from the <application> element.
 */
fun Manifest.removeProperty(vararg properties: String) =
    removeManifestElements("property", *properties)
