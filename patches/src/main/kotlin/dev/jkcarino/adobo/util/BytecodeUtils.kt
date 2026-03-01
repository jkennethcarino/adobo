package dev.jkcarino.adobo.util

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.analysis.reflection.util.ReflectionUtils
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.Reference
import com.android.tools.smali.dexlib2.iface.value.EncodedValue
import com.android.tools.smali.dexlib2.util.MethodUtil

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
 * Finds and returns the first matching method in the class hierarchy that
 * matches the given [method].
 *
 * @receiver MutableClass The class to search for the method.
 * @param method The method to match.
 * @return The first matching mutable method.
 */
fun MutableClass.findMutableMethodOf(method: Method) = this.methods.first {
    MethodUtil.methodSignaturesMatch(it, method)
}

/**
 * Returns the [Reference] as [T] or null if the [Instruction] is not a
 * [ReferenceInstruction] or the [Reference] is not of type [T].
 *
 * @receiver Instruction The instruction to extract the reference from.
 * @return The reference as type [T], or null if not applicable.
 */
inline fun <reified T : Reference> Instruction.getReference() =
    (this as? ReferenceInstruction)?.reference as? T

/**
 * Returns the [Field]'s initial value as [T] or null if the initial value is not of type [T].
 *
 * @receiver Field The field to extract the encoded value from.
 * @return The encoded value as type [T], or null if not applicable.
 */
inline fun <reified T : EncodedValue> Field.getEncodedValue() =
    this.initialValue as? T

/**
 * Traverses the class hierarchy starting from the given root [targetClass].
 *
 * This first calls the provided [callback] with the [targetClass] as the argument. It then
 * recursively traverses the class hierarchy by finding the superclass of the [targetClass]
 * and calling this function with the superclass as the new [targetClass]. The recursion continues
 * until there are no more superclasses.
 * and recursively traversing superclasses.
 *
 * @receiver BytecodePatchContext The patch context used for class lookup.
 * @param targetClass The root class to start traversal from.
 * @param callback The function to call for each class in the hierarchy.
 */
fun BytecodePatchContext.traverseClassHierarchy(
    targetClass: MutableClass,
    callback: MutableClass.() -> Unit,
) {
    callback(targetClass)

    targetClass.superclass ?: return

    mutableClassDefByOrNull { targetClass.superclass == it.type }
        ?.let { traverseClassHierarchy(it, callback) }
}

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
fun MutableMethod.returnEarly() =
    when (returnType) {
        "V" -> addInstruction(0, "return-void")
        "C" -> returnEarly(Char.MIN_VALUE)
        "B" -> returnEarly(0.toByte())
        "Z" -> returnEarly(false)
        "S" -> returnEarly(0.toShort())
        "I" -> returnEarly(0)
        "F" -> returnEarly(0f)
        "J" -> returnEarly(0L)
        "D" -> returnEarly(0.0)
        else -> addInstruction(0, objectReturn("0x0"))
    }

/**
 * Injects an early return of a `String` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The string to be returned.
 */
fun MutableMethod.returnEarly(value: String) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Char` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The character to be returned.
 */
fun MutableMethod.returnEarly(value: Char) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Byte` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The byte to be returned.
 */
fun MutableMethod.returnEarly(value: Byte) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Boolean` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The boolean to be returned.
 */
fun MutableMethod.returnEarly(value: Boolean) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Short` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The short to be returned.
 */
fun MutableMethod.returnEarly(value: Short) = injectEarlyReturn(value)

/**
 * Injects an early return of an `Int` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The integer to be returned.
 */
fun MutableMethod.returnEarly(value: Int) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Float` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The float to be returned.
 */
fun MutableMethod.returnEarly(value: Float) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Long` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The long to be returned.
 */
fun MutableMethod.returnEarly(value: Long) = injectEarlyReturn(value)

/**
 * Injects an early return of a `Double` value into the mutable method.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The double to be returned.
 */
fun MutableMethod.returnEarly(value: Double) = injectEarlyReturn(value)

/**
 * Verifies that the method’s declared return type matches the type of the
 * supplied `expectedType`. The check works for both primitive JVM types and
 * their boxed equivalents.
 *
 * @receiver MutableMethod The method whose return type is checked.
 * @param T The expected Kotlin type.
 * @throws IllegalStateException if the method’s return type does not match either the
 *   primitive or boxed form of `expectedType`.
 */
private fun <T : Any> MutableMethod.checkReturnType(expectedType: T) {
    val javaName = ReflectionUtils.dexToJavaName(returnType)
    val primitiveMatch = javaName == expectedType::class.javaPrimitiveType?.name
    val boxedMatch = javaName == expectedType::class.javaObjectType.name

    check(primitiveMatch || boxedMatch) {
        "Return type mismatch: expected ${expectedType::class.javaObjectType} or " +
            "${expectedType::class.javaPrimitiveType}, got $javaName"
    }
}

/**
 * Injects an early return of the given value into the mutable method, verifying type safety.
 *
 * @receiver MutableMethod The method to inject the early return into.
 * @param value The value to be returned.
 */
private fun <T : Any> MutableMethod.injectEarlyReturn(value: T) {
    checkReturnType(value)
    addInstructions(
        index = 0,
        smaliInstructions = buildReturnInstructions(value)
    )
}

/**
 * Builds the smali instructions for returning the given value from the method. Handles primitive,
 * boxed, and wide types.
 *
 * @receiver MutableMethod The method to build return instructions for.
 * @param value The value to be returned.
 * @return The smali instructions as a string.
 */
private fun <T : Any> MutableMethod.buildReturnInstructions(value: T): String {
    val typeCode = value::class.javaPrimitiveType?.dexName
    val boxed = value::class.javaObjectType.dexName

    return when (value) {
        is String -> objectReturn(value.toString())
        is Byte, is Int -> primitiveOrBoxedReturn(
            value = value.toString(),
            typeCode = typeCode!!,
            boxed = boxed
        )
        is Short, is Char -> primitiveOrBoxedReturn(
            value = value.toString(),
            typeCode = typeCode!!,
            boxed = boxed,
            bitWidth = 16
        )
        is Long, is Double -> wideReturn(
            value = value.toString(),
            typeCode = typeCode!!,
            boxed = boxed
        )
        is Float -> primitiveOrBoxedReturn(
            value = "${value}f",
            typeCode = typeCode!!,
            boxed = boxed
        )
        is Boolean -> primitiveOrBoxedReturn(
            value = value.toHexString(),
            typeCode = typeCode!!,
            boxed = boxed
        )
        else -> error("Cannot return $value")
    }
}

/**
 * Generates object return instructions for a given value.
 *
 * @param value The value to be returned as a string.
 * @return A string representation of the object return instructions.
 */
private fun objectReturn(value: String): String {
    return """
        const/4 v0, $value
        return-object v0
    """
}

/**
 * Builds smali instructions for returning a primitive or boxed value.
 *
 * @receiver MutableMethod The method to build return instructions for.
 * @param value The raw value as a string.
 * @param typeCode The type code for the primitive type.
 * @param boxed The boxed type name.
 * @param bitWidth The bit width for the constant (default is 4).
 * @return The smali instructions as a string.
 */
private fun MutableMethod.primitiveOrBoxedReturn(
    value: String,
    typeCode: String,
    boxed: String,
    bitWidth: Int = 4,
): String {
    return if (returnType == typeCode) {
        """
            const/$bitWidth v0, $value
            return v0
        """
    } else {
        """
            const/$bitWidth v0, $value
            invoke-static {v0}, $boxed->valueOf($typeCode)$boxed
            move-result-object v0
            return-object v0
        """
    }
}

/**
 * Constructs return instructions for wide types (Long, Double).
 *
 * @receiver MutableMethod The method to build return instructions for.
 * @param value The raw string representation of the value.
 * @param typeCode The type code of the raw value.
 * @param boxed The boxed type representation of the raw value.
 * @return A string representation of the wide return instructions.
 */
private fun MutableMethod.wideReturn(
    value: String,
    typeCode: String,
    boxed: String,
): String {
    return if (returnType == typeCode) {
        """
            const-wide v0, ${value}$typeCode
            return-wide v0
        """
    } else {
        """
            const-wide v0, ${value}$typeCode
            invoke-static {v0}, $boxed->valueOf($typeCode)$boxed
            move-result-object v0
            return-object v0
        """
    }
}
