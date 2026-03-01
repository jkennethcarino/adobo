package dev.jkcarino.adobo.patches.all.detection.signature.pms

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption

@Suppress("unused")
val spoofSignatureVerificationPatch = bytecodePatch(
    name = "Spoof signature verification",
    description = "Spoofs the signature verification when the app starts up. " +
        "It is recommended to use the unmodified app to work properly.",
    use = false
) {
    extendWith("extensions/all/detection/signature/pms.mpe")

    dependsOn(
        packageNamePatch,
        encodeCertificatePatch,
        replaceSubApplicationPatch
    )

    val packageNameOption =
        stringOption(
            key = "packageName",
            default = "Default",
            values = mapOf("Default" to "Default"),
            title = "Package name",
            description = "The package name of the app, if modified. " +
                "This must match the package name defined when the " +
                "'Change package name' patch is selected or as defined in the AndroidManifest.xml.",
            required = true,
        ) { packageName ->
            val packageNamePattern = """^[a-z]\w*(\.[a-z]\w*)+$""".toRegex()
            packageName == "Default" || packageName!!.matches(packageNamePattern)
        }

    val signatureOption =
        stringOption(
            key = "signature",
            default = "Default",
            values = mapOf("Default" to "Default"),
            title = "Base64-encoded signature",
            description = "The base64-encoded signature from the original, unmodified APK. " +
                "This extracts the certificate/signature in the APK by default if blank.",
            required = true,
        ) { signature ->
            signature == "Default" || !signature.isNullOrEmpty()
        }

    execute {
        StaticConstructorFingerprint.method.apply {
            val packageNameIndex = StaticConstructorFingerprint.instructionMatches.first().index
            val customPackageName = packageNameOption.value!!
            val packageName =
                if (customPackageName == packageNameOption.default) {
                    appPackageName
                } else {
                    customPackageName
                }

            replaceInstruction(
                index = packageNameIndex,
                smaliInstruction = """
                    const-string v0, "$packageName"
                """
            )

            val signatureIndex = StaticConstructorFingerprint.instructionMatches.last().index
            val customSignature = signatureOption.value!!
            val signature =
                if (customSignature == signatureOption.default) {
                    signature ?: throw PatchException(
                        errorMessage = "Please provide a valid signature encoded in Base64."
                    )
                } else {
                    customSignature.trim()
                }

            replaceInstruction(
                index = signatureIndex,
                smaliInstruction = """
                    const-string v1, "$signature"
                """
            )
        }

        val signatureHookAppClass = StaticConstructorFingerprint.originalClassDef

        classDefForEach { classDef ->
            if (
                classDef != signatureHookAppClass &&
                classDef.superclass == "Landroid/app/Application;"
            ) {
                mutableClassDefBy(classDef)
                    .setSuperClass(signatureHookAppClass.type)
            }
        }
    }
}
