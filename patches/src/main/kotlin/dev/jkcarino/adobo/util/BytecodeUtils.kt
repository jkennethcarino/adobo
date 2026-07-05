package dev.jkcarino.adobo.util

import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.value.EncodedValue

/**
 * Finds and returns the first matching field in the class that matches the given [field].
 *
 * @receiver MutableClass The class to search for the field.
 * @param field The field to match.
 * @return The first matching mutable field.
 */
fun MutableClass.findMutableFieldOf(field: Field) = this.fields.first {
    it.name == field.name && it.type == field.type
}

/**
 * Returns the [Field]'s initial value as [T] or null if the initial value is not of type [T].
 *
 * @receiver Field The field to extract the encoded value from.
 * @return The encoded value as type [T], or null if not applicable.
 */
inline fun <reified T : EncodedValue> Field.getEncodedValue() =
    this.initialValue as? T

/**
 * Filters methods of this class based on the [predicate]. Only methods with
 * non-null instructions are considered.
 *
 * @receiver ClassDef The class whose methods will be filtered.
 * @param predicate The predicate to determine if a method should be included.
 * @return List of methods that match the predicate and have instructions.
 */
fun ClassDef.filterMethods(
    predicate: (ClassDef, Method) -> Boolean,
): List<Method> = buildList {
    val classDef = this@filterMethods
    methods.forEach { method ->
        method.instructionsOrNull ?: return@forEach
        if (predicate(classDef, method)) {
            add(method)
        }
    }
}

/**
 * Filters methods from all classes in the list based on the [predicate]. Only methods with
 * non-null instructions are considered.
 *
 * @receiver List<ClassDef> The list of classes to filter methods from.
 * @param predicate The predicate to determine if a method should be included.
 * @return List of methods that match the predicate and have instructions.
 */
fun List<ClassDef>.filterMethods(
    predicate: (ClassDef, Method) -> Boolean,
): List<Method> = flatMap { it.filterMethods(predicate) }

/**
 * Injects an early return into the mutable method, returning the
 * default value for the method's return type.
 *
 * @receiver MutableMethod The method to inject the early return into.
 */
fun MutableMethod.defaultReturnEarly() =
    when (returnType) {
        "V" -> returnEarly()
        "C" -> returnEarly(Char.MIN_VALUE)
        "B" -> returnEarly(0.toByte())
        "Z" -> returnEarly(false)
        "S" -> returnEarly(0.toShort())
        "I" -> returnEarly(0)
        "F" -> returnEarly(0f)
        "J" -> returnEarly(0L)
        "D" -> returnEarly(0.0)
        else -> returnEarly(null)
    }
