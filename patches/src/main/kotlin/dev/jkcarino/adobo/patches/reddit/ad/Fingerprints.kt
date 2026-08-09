package dev.jkcarino.adobo.patches.reddit.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation.MatchAfterImmediately
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef
import dev.jkcarino.adobo.util.constClass

internal const val EXTENSION_CLASS_DESCRIPTOR =
    "Ldev/jkcarino/extension/reddit/frontpage/AdBlockInterceptor;"

internal object InterceptFingerprint : Fingerprint(
    definingClass = EXTENSION_CLASS_DESCRIPTOR,
    returnType = "Lokhttp3/Response;",
    parameters = listOf($$"Lokhttp3/Interceptor$Chain;"),
    filters = OpcodesFilter.opcodesToFilters(
        // responseBody.source()
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,

        // source.request(Long.MAX_VALUE)
        Opcode.CONST_WIDE,
        Opcode.INVOKE_INTERFACE,

        // source.getBuffer()
        Opcode.INVOKE_INTERFACE,
        Opcode.MOVE_RESULT_OBJECT,
        // .clone()
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,

        // contentType.charset(StandardCharsets.UTF_8)
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,

        // buffer.readString(charset)
        Opcode.INVOKE_VIRTUAL,
    )
)

internal object OkHttpConstructorFingerprint : Fingerprint(
    definingClass = "Lokhttp3/OkHttpClient;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.CONSTRUCTOR),
    parameters = listOf($$"Lokhttp3/OkHttpClient$Builder;"),
    filters = listOf(
        opcode(Opcode.MOVE_RESULT_OBJECT),
        opcode(Opcode.IPUT_OBJECT, MatchAfterImmediately()),
        anyInstruction(
            opcode(Opcode.INVOKE_VIRTUAL),
            opcode(Opcode.INVOKE_VIRTUAL_RANGE),
            location = MatchAfterImmediately()
        ),
        opcode(Opcode.MOVE_RESULT_OBJECT, MatchAfterImmediately())
    )
)

internal object RealBufferedSourceCommonIndexOfFingerprint : Fingerprint(
    returnType = "J",
    parameters = listOf("B", "J", "J"),
    filters = listOf(
        string("fromIndex=0 toIndex="),
        string("closed")
    )
)

internal object BufferCommonReadAndWriteUnsafeFingerprint : Fingerprint(
    parameters = listOf("L"),
    filters = listOf(
        string("already attached to a buffer")
    )
)

internal object BufferReadStringFingerprint : Fingerprint(
    classFingerprint = BufferCommonReadAndWriteUnsafeFingerprint,
    returnType = "Ljava/lang/String;",
    parameters = listOf("Ljava/nio/charset/Charset;")
)

internal object BufferCloneFingerprint : Fingerprint(
    classFingerprint = BufferCommonReadAndWriteUnsafeFingerprint,
    name = "clone",
    accessFlags = listOf(
        AccessFlags.PUBLIC,
        AccessFlags.FINAL,
        AccessFlags.BRIDGE,
        AccessFlags.SYNTHETIC
    ),
    filters = listOf(
        opcode(Opcode.INVOKE_VIRTUAL)
    )
)

internal val bufferedSourceGetBufferFingerprint = { classDef: ClassDef ->
    Fingerprint(
        classFingerprint = RealBufferedSourceCommonIndexOfFingerprint,
        returnType = classDef.type,
        parameters = listOf(),
        filters = listOf(
            opcode(Opcode.RETURN_OBJECT)
        )
    )
}

internal object AdElementToStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
    filters = listOf(
        string("AdElement(linkId="),
    )
)

internal val adConverterFingerprint = { adElementDefiningClass: String ->
    Fingerprint(
        name = "<init>",
        filters = listOf(
            constClass(adElementDefiningClass),
            methodCall(
                definingClass = "Lkotlin/jvm/internal/",
                parameters = listOf("Ljava/lang/Class;")
            )
        )
    )
}

internal val adConverterConvertFingerprint = { classFingerprint: Fingerprint ->
    Fingerprint(
        classFingerprint = classFingerprint,
        returnType = "L",
        parameters = listOf("L", "L")
    )
}

internal object IsAdEligibleFingerprint : Fingerprint(
    definingClass = "Lcom/reddit/ads/impl/commentspage/placeholder/",
    returnType = "Z",
    parameters = listOf("Ljava/lang/String;"),
    filters = listOf(
        fieldAccess(
            definingClass = "Lcom/reddit/type/PostAdEligibilityStatus;",
            name = "ELIGIBLE"
        ),
        fieldAccess(
            definingClass = "Lcom/reddit/type/UserAdEligibilityStatus;",
            name = "ELIGIBLE"
        )
    )
)
