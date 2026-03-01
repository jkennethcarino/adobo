package dev.jkcarino.adobo.patches.all.detection.signature.pms

import app.morphe.patcher.patch.rawResourcePatch
import java.io.File
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Base64
import java.util.logging.Logger

internal var signature: String? = null
    private set

val encodeCertificatePatch = rawResourcePatch(
    description = "Extracts and encodes the digital certificate/signature to Base64."
) {
    execute {
        fun File.isCertificate(): Boolean {
            return isFile && extension in setOf("RSA", "DSA", "EC")
        }

        val certificateFile = get("META-INF").listFiles()
            ?.firstOrNull(File::isCertificate)

        if (certificateFile == null) {
            return@execute Logger
                .getLogger(this::class.java.name)
                .info("No META-INF/*.RSA, .DSA, or .EC found in APK.")
        }

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificates = certificateFile
            .inputStream()
            .use { inputStream ->
                certificateFactory
                    .generateCertificates(inputStream)
                    .filterIsInstance<X509Certificate>()
            }

        signature = certificates
            .joinToString { certificate ->
                Base64
                    .getEncoder()
                    .encodeToString(certificate.encoded)
            }
    }
}
