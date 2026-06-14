package dev.jkcarino.adobo.patches.reddit.ad

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val removeAdsAndTelemetryPatch = bytecodePatch(
    name = "Remove ads and telemetry",
    description = "Removes ads and telemetry everywhere."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

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

        InterceptFingerprint.method.apply {
            val realBufferedSourceClassDef =
                RealBufferedSourceCommonIndexOfFingerprint.originalClassDef
            val bufferedSource = realBufferedSourceClassDef.interfaces.first()
            val bufferClassDef = BufferCommonReadAndWriteUnsafeFingerprint.originalClassDef
            val buffer = bufferClassDef.type

            val getBuffer = bufferedSourceGetBufferFingerprint(bufferClassDef).method.name

            val realCloneInstruction = BufferCloneFingerprint.instructionMatches.first()
            val realClone = realCloneInstruction.getMethodCalled().name

            val readString = BufferReadStringFingerprint.method.name

            // responseBody.source()
            val responseBodySourceIndex = InterceptFingerprint.instructionMatches.first().index
            val responseBodySourceInstruction =
                getInstruction<FiveRegisterInstruction>(responseBodySourceIndex)
            val responseBodySourceSmali =
                "invoke-virtual {v${responseBodySourceInstruction.registerC}}, " +
                    "Lokhttp3/ResponseBody;->source()$bufferedSource"

            // source.request(Long.MAX_VALUE)
            val sourceRequestIndex = responseBodySourceIndex + 3
            val sourceRequestInstruction =
                getInstruction<FiveRegisterInstruction>(sourceRequestIndex)
            val sourceRequestSmali =
                buildString {
                    append("invoke-interface {")
                    append("v${sourceRequestInstruction.registerC}, ")
                    append("v${sourceRequestInstruction.registerD}, ")
                    append("v${sourceRequestInstruction.registerE}")
                    append("}, $bufferedSource->request(J)Z")
                }

            // source.getBuffer()
            val sourceGetBufferIndex = sourceRequestIndex + 1
            val sourceGetBufferInstruction =
                getInstruction<FiveRegisterInstruction>(sourceGetBufferIndex)
            val sourceGetBufferSmali =
                "invoke-interface {v${sourceGetBufferInstruction.registerC}}, " +
                    "$bufferedSource->$getBuffer()$buffer"

            // .clone()
            val bufferCloneIndex = sourceGetBufferIndex + 2
            val bufferCloneInstruction = getInstruction<FiveRegisterInstruction>(bufferCloneIndex)
            val bufferCloneSmali =
                "invoke-virtual {v${bufferCloneInstruction.registerC}}, " +
                    "$buffer->$realClone()$buffer"

            // buffer.readString(charset)
            val bufferReadStringIndex = InterceptFingerprint.instructionMatches.last().index
            val bufferReadStringInstruction =
                getInstruction<FiveRegisterInstruction>(bufferReadStringIndex)
            val bufferReadStringSmali =
                "invoke-virtual {v${bufferReadStringInstruction.registerC}, " +
                    "v${bufferReadStringInstruction.registerD}}, " +
                    "$buffer->$readString(Ljava/nio/charset/Charset;)Ljava/lang/String;"

            mapOf(
                responseBodySourceIndex to responseBodySourceSmali,
                sourceRequestIndex to sourceRequestSmali,
                sourceGetBufferIndex to sourceGetBufferSmali,
                bufferCloneIndex to bufferCloneSmali,
                bufferReadStringIndex to bufferReadStringSmali
            ).forEach { (index, smali) ->
                replaceInstruction(index, smali)
            }
        }
    }
}
