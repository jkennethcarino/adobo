package dev.jkcarino.adobo.patches.reddit.layout.suspendedbanner

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.packageMetadata
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val hideSuspendedBannerPatch = patch(
    name = "Hide suspended account banner",
    description = "Hides the temporary and permanent account suspension banners on the " +
        "home and inbox screens.",
    default = false
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    dependsOn(spoofCertificateHashPatch)

    execute {
        val versionName = packageMetadata.versionName

        when {
            versionName <= "2026.24.0" -> {
                CheckSuspendedReasonFingerprint.method.apply {
                    val trueIndex =
                        CheckSuspendedReasonFingerprint.instructionMatches.last().index
                    val trueRegister =
                        getInstruction<OneRegisterInstruction>(trueIndex).registerA

                    replaceInstruction(
                        index = trueIndex,
                        smaliInstruction = """
                            sget-object v$trueRegister, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                        """
                    )
                }
                OnCreateViewFingerprint.method.apply {
                    val isSuspendedIndex =
                        OnCreateViewFingerprint.instructionMatches.last().index
                    val isSuspendedRegister =
                        getInstruction<OneRegisterInstruction>(isSuspendedIndex).registerA

                    addInstruction(
                        index = isSuspendedIndex + 1,
                        smaliInstructions = "const/4 v$isSuspendedRegister, 0x0"
                    )
                }
            }
            versionName == "2026.25.0" -> {
                ShouldShowSuspendedBannerFingerprint.method.apply {
                    val accountNotNullIndex =
                        ShouldShowSuspendedBannerFingerprint.instructionMatches.first().index
                    val accountRegister =
                        getInstruction<OneRegisterInstruction>(accountNotNullIndex).registerA

                    addInstructions(
                        index = accountNotNullIndex,
                        smaliInstructions = """
                            const/4 v$accountRegister, 0x0
                            return v$accountRegister
                        """
                    )
                }
            }
            else -> {
                ShouldShowSuspendedBannerV2Fingerprint.method.apply {
                    val accountNotNullIndex =
                        ShouldShowSuspendedBannerV2Fingerprint.instructionMatches.first().index

                    addInstructions(
                        index = accountNotNullIndex,
                        smaliInstructions = """
                            sget-object p0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                            return-object p0
                        """
                    )
                }
            }
        }
    }
}
