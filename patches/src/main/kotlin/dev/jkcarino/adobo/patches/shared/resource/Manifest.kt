package dev.jkcarino.adobo.patches.shared.resource

import app.morphe.patcher.patch.resourceXml
import app.morphe.patcher.xml.ResourceXml
import app.morphe.patcher.xml.XmlElement
import dev.jkcarino.adobo.util.get
import dev.jkcarino.adobo.util.set

typealias Manifest = ResourceXml

private const val MANIFEST_NODE = "manifest"
internal const val APPLICATION_NODE = "application"
internal const val META_DATA_TAG = "meta-data"
internal const val ANDROID_NAME_ATTR = "android:name"
internal const val ANDROID_VALUE_ATTR = "android:value"

/**
 * Applies a configuration block to the AndroidManifest.
 */
fun androidManifest(block: Manifest.() -> Unit): ResourceXml =
    resourceXml("AndroidManifest.xml").use { xml ->
        xml.apply(block)
    }

/**
 * Removes elements from the manifest based on the specified tag name and element names.
 */
private fun Manifest.removeManifestElements(
    tagName: String,
    vararg elements: String,
    isRootLevel: Boolean = false,
) {
    val nodeName = if (isRootLevel) MANIFEST_NODE else APPLICATION_NODE
    val node = this[nodeName]
    val regexes = elements.map(String::toRegex)

    node.elements(tagName)
        .filter { element ->
            val androidName = element[ANDROID_NAME_ATTR]
            regexes.any { it.matches(androidName) }
        }
        .forEach(XmlElement::remove)
}

/**
 * Adds or updates meta-data elements in the manifest.
 */
fun Manifest.metaData(vararg properties: Pair<String, String>) {
    val application = this[APPLICATION_NODE]

    properties.forEach { (name, value) ->
        val metaData = application
            .elements(META_DATA_TAG)
            .firstOrNull { it[ANDROID_NAME_ATTR] == name }

        if (metaData != null) {
            metaData[ANDROID_VALUE_ATTR] = value
        } else {
            application.appendChild(META_DATA_TAG) {
                this[ANDROID_NAME_ATTR] = name
                this[ANDROID_VALUE_ATTR] = value
            }
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
