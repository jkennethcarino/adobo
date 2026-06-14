package dev.jkcarino.adobo.patches.reddit.layout.flair

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT
import dev.jkcarino.adobo.patches.reddit.shared.util.updateClassField
import dev.jkcarino.adobo.util.filterMethods
import dev.jkcarino.adobo.util.findMutableMethodOf
import dev.jkcarino.adobo.util.getReference
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val hideUserFlairsPatch = bytecodePatch(
    name = "Hide user flairs",
    description = "Hides the user flairs on Reddit comments.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        setOf(
            commentPresentationModelToStringFingerprints,
            commentToStringFingerprints
        ).flatten().forEach { fingerprint ->
            val userFlairIndex = fingerprint.instructionMatches.last().index
            val userFlairInstruction =
                fingerprint.method.getInstruction<TwoRegisterInstruction>(userFlairIndex)
            val userFlairFieldReference = userFlairInstruction.getReference<FieldReference>()!!

            updateClassField(
                classDef = fingerprint.classDef,
                fieldReference = userFlairFieldReference,
                value = null
            )
        }

        val userFlairMethods = setOf(
            "getAuthorFlairText" to "Ljava/lang/String;",
            "getAuthorFlairRichText" to "Ljava/util/List;"
        )

        classDefForEach { classDef ->
            classDef
                .filterMethods filter@{ _, method ->
                    userFlairMethods.any { (methodName, returnType) ->
                        methodName == method.name &&
                            returnType == method.returnType
                    }
                }
                .forEach { method ->
                    mutableClassDefBy(method.definingClass)
                        .findMutableMethodOf(method)
                        .returnEarly()
                }
        }
    }
}
