package dev.jkcarino.adobo.util

import app.morphe.patcher.xml.ResourceXml
import app.morphe.patcher.xml.XmlElement

/**
 * Returns the first element in the [ResourceXml] with the specified tag name.
 */
operator fun ResourceXml.get(tagName: String): XmlElement = elements(tagName).first()

/**
 * Returns the value of the specified attribute of the [XmlElement].
 */
operator fun XmlElement.get(attrName: String): String = attribute(attrName)!!

/**
 * Sets the value of the specified attribute of the [XmlElement].
 */
operator fun XmlElement.set(
    attrName: String,
    attrValue: String,
): Unit = setAttribute(attrName, attrValue)
