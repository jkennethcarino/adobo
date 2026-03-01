package dev.jkcarino.adobo.patches.reddit.ad

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch

private const val EXTENSION_CLASS_DESCRIPTOR =
    "Ldev/jkcarino/extension/reddit/frontpage/AdBlockInterceptor;"

@Suppress("unused")
val removeAdsAndTelemetryPatch = bytecodePatch(
    name = "Remove ads and telemetry",
    description = "Removes ads and telemetry everywhere.",
    use = true
) {
    compatibleWith("com.reddit.frontpage")

    extendWith("extensions/reddit/frontpage.mpe")

    dependsOn(spoofCertificateHashPatch)

    execute {
        OkHttpConstructorFingerprint.method.apply {
            val interceptorsIndex = OkHttpConstructorFingerprint.instructionMatches.last().index
            val interceptorsInstruction = getInstruction<OneRegisterInstruction>(interceptorsIndex)
            val interceptorsRegister = interceptorsInstruction.registerA
            val adBlockInterceptorRegister = interceptorsRegister + 1

            addInstructions(
                index = interceptorsIndex + 1,
                smaliInstructions = """
                    invoke-static {}, $EXTENSION_CLASS_DESCRIPTOR->getInstance()$EXTENSION_CLASS_DESCRIPTOR
                    move-result-object v$adBlockInterceptorRegister
                    invoke-virtual {v$adBlockInterceptorRegister, v$interceptorsRegister}, $EXTENSION_CLASS_DESCRIPTOR->inject(Ljava/util/List;)V
                """
            )
        }

        val adBlockInterceptorClass = classDefBy(EXTENSION_CLASS_DESCRIPTOR)

        InterceptFingerprint.match(adBlockInterceptorClass).method.apply {
            val realBufferedSourceClassDef =
                RealBufferedSourceCommonIndexOfFingerprint.originalClassDef
            val bufferedSource = realBufferedSourceClassDef.interfaces.first()
            val bufferClassDef = BufferCommonReadAndWriteUnsafeFingerprint.originalClassDef
            val buffer = bufferClassDef.type

            val getBuffer = bufferedSourceGetBufferFingerprint(bufferClassDef)
                .match(realBufferedSourceClassDef)
                .method
                .name

            val cloneMethod = BufferCloneFingerprint.match(bufferClassDef).method
            val realClone = navigate(cloneMethod).to(0).original().name

            val readString = BufferReadStringFingerprint
                .match(bufferClassDef)
                .method
                .name

            val responseBodySourceIndex = InterceptFingerprint.instructionMatches.first().index
            val sourceRequestIndex = responseBodySourceIndex + 3
            val sourceGetBufferIndex = sourceRequestIndex + 1
            val bufferCloneIndex = sourceGetBufferIndex + 2
            val bufferReadStringIndex = InterceptFingerprint.instructionMatches.last().index

            mapOf(
                responseBodySourceIndex to
                    "invoke-virtual {v0}, Lokhttp3/ResponseBody;->source()$bufferedSource",
                sourceRequestIndex to
                    "invoke-interface {v0, v2, v3}, $bufferedSource->request(J)Z",
                sourceGetBufferIndex to
                    "invoke-interface {v0}, $bufferedSource->$getBuffer()$buffer",
                bufferCloneIndex to
                    "invoke-virtual {v0}, $buffer->$realClone()$buffer",
                bufferReadStringIndex to
                    "invoke-virtual {v0, v2}, $buffer->$readString(Ljava/nio/charset/Charset;)Ljava/lang/String;"
            ).forEach { (index, smali) ->
                replaceInstruction(index, smali)
            }
        }
    }
}
