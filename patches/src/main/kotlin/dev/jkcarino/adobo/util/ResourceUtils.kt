package dev.jkcarino.adobo.util

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Returns the first element in the [Document] with the specified tag name.
 */
operator fun Document.get(tagName: String): Element =
    getElementsByTagName(tagName).item(0) as Element

/**
 * Returns the value of the specified attribute of the [Element].
 */
operator fun Element.get(attrName: String): String = getAttribute(attrName)

/**
 * Sets the value of the specified attribute of the [Element].
 */
operator fun Element.set(
    attrName: String,
    attrValue: String,
): Unit = setAttribute(attrName, attrValue)

/**
 * Filters the child nodes to return a sequence of only those that are [Element]s.
 */
fun NodeList.asElementSequence(): Sequence<Element> =
    (0 until this.length)
        .asSequence()
        .mapNotNull { this.item(it) as? Element }

/**
 * Converts the [NamedNodeMap] to a [Sequence] of [Node] objects, representing the
 * attributes of the [NamedNodeMap].
 */
fun NamedNodeMap.asAttributeSequence(): Sequence<Node> =
    (0 until this.length)
        .asSequence()
        .mapNotNull { this.item(it) }

/**
 * Filters the elements in a [NodeList] based on the given [predicate].
 */
fun NodeList.filterElements(predicate: (Element) -> Boolean): List<Element> =
    asElementSequence().filter(predicate).toList()

/**
 * Removes a list of child elements from the current [Node].
 */
fun Node.removeElements(elements: List<Element>) {
    elements.forEach { element ->
        this@removeElements.removeChild(element)
    }
}

/**
 * Creates a new element with the specified tag name and attributes.
 */
inline fun Document.createElement(
    tagName: String,
    init: Element.() -> Unit,
): Element {
    val element = createElement(tagName)
    element.init()
    return element
}
