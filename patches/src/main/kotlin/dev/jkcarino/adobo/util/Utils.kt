package dev.jkcarino.adobo.util

/**
 * Converts a nullable Boolean to its Dex hex string representation.
 *
 * @receiver Boolean? The nullable boolean value to convert.
 * @return "0x1" if true, "0x0" if false or null.
 */
internal fun Boolean?.toHexString(): String =
    if (this == true) "0x1" else "0x0"
