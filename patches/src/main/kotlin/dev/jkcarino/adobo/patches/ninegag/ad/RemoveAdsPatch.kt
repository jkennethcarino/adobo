package dev.jkcarino.adobo.patches.ninegag.ad

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.getReference
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlocker
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.HostsBlockerConfig
import dev.jkcarino.adobo.patches.all.contentblocker.hosts.baseHostsBlockerPatch
import dev.jkcarino.adobo.patches.ninegag.shared.COMPATIBILITY_NINEGAG

@Suppress("unused")
val removeAdsPatch = bytecodePatch(
    name = "Remove 9GAG's ads, trackers, and analytics",
    description = "Removes ads, trackers, and analytics in the 9GAG app."
) {
    compatibleWith(COMPATIBILITY_NINEGAG)

    dependsOn(
        baseHostsBlockerPatch {
            HostsBlockerConfig(
                hostsBlocker = HostsBlocker.fromString(AD_HOSTS)
            )
        },
        hideAdContainersPatch
    )

    execute {
        val unitIndex = UnitFingerprint.instructionMatches.last().index
        val unitInstruction =
            UnitFingerprint.method.getInstruction<OneRegisterInstruction>(unitIndex)
        val unit = unitInstruction.getReference<FieldReference>()!!

        InitializeFingerprint.method.replaceInstructions(
            index = 0,
            smaliInstructions = """
                sget-object v0, ${unit.definingClass}->${unit.name}:${unit.type}
                return-object v0
            """
        )

        ShouldShowAdsFingerprint.method.returnEarly(false)
    }
}
