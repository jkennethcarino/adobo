package dev.jkcarino.adobo.patches.reddit.layout.screenshotbanner

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.util.returnEarly

@Suppress("unused")
val disableScreenshotBannerPatch = bytecodePatch(
    name = "Disable screenshot banner",
    description = "Disables the banner that shows up after taking a screenshot.",
    use = true
) {
    compatibleWith("com.reddit.frontpage")

    dependsOn(spoofCertificateHashPatch)

    execute {
        OnScreenCapturedFingerprint.methodOrNull?.returnEarly()

        setOf(
            ScreenshotBannerInvokeSuspendFingerprint,
            ScreenshotTakenBannerInvokeSuspendFingerprint
        ).forEach { fingerprint ->
            fingerprint.method.apply {
                val booleanIndex = fingerprint.instructionMatches.last().index - 1
                val booleanRegister =
                    getInstruction<OneRegisterInstruction>(booleanIndex).registerA

                replaceInstruction(
                    index = booleanIndex,
                    smaliInstruction = """
                         sget-object v$booleanRegister, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                    """
                )
            }
        }
    }
}
