package dev.jkcarino.adobo.patches.reddit.ad

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef

internal object InterceptFingerprint : Fingerprint(
    returnType = "Lokhttp3/Response;",
    parameters = listOf("Lokhttp3/Interceptor\$Chain;"),
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
    parameters = listOf("Lokhttp3/OkHttpClient\$Builder;"),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.CONST_STRING,
        Opcode.INVOKE_STATIC,
        null,
        null,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.IPUT_OBJECT,
        null,
        Opcode.MOVE_RESULT_OBJECT,
    )
)

internal object RealBufferedSourceCommonIndexOfFingerprint : Fingerprint(
    returnType = "J",
    parameters = listOf("B", "J", "J"),
    strings = listOf(
        "fromIndex=0 toIndex=",
        "closed",
    )
)

internal object BufferCommonReadAndWriteUnsafeFingerprint : Fingerprint(
    returnType = "L",
    parameters = listOf("L"),
    strings = listOf("already attached to a buffer")
)

internal object BufferReadStringFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    parameters = listOf("Ljava/nio/charset/Charset;"),
    strings = listOf("charset")
)

internal object BufferCloneFingerprint : Fingerprint(
    name = "clone",
    accessFlags = listOf(
        AccessFlags.PUBLIC,
        AccessFlags.FINAL,
        AccessFlags.BRIDGE,
        AccessFlags.SYNTHETIC
    )
)

internal val bufferedSourceGetBufferFingerprint = { classDef: ClassDef ->
    Fingerprint(
        returnType = classDef.type,
        parameters = listOf(),
        filters = listOf(
            opcode(Opcode.RETURN_OBJECT)
        )
    )
}
