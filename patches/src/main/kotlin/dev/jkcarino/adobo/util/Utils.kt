package dev.jkcarino.adobo.util

import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils

/**
 * Gets the Dex type name for a Java class.
 *
 * @receiver Class<T> The Java class to convert.
 * @return The Dex type name as a string.
 */
internal val <T : Any> Class<T>.dexName: String
    get() = ReflectionUtils.javaToDexName(this.name)

/**
 * Converts a nullable Boolean to its Dex hex string representation.
 *
 * @receiver Boolean? The nullable boolean value to convert.
 * @return "0x1" if true, "0x0" if false or null.
 */
internal fun Boolean?.toHexString(): String =
    if (this == true) "0x1" else "0x0"
