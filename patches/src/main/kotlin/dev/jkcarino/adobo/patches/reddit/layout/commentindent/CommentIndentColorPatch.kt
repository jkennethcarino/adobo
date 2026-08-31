package dev.jkcarino.adobo.patches.reddit.layout.commentindent

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.booleanOption
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.colorOption
import com.android.tools.smali.dexlib2.builder.instruction.BuilderArrayPayload
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction31t
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import dev.jkcarino.adobo.patches.reddit.misc.firebase.spoofCertificateHashPatch
import dev.jkcarino.adobo.patches.reddit.shared.COMPATIBILITY_REDDIT

@Suppress("unused")
val commentIndentColorPatch = bytecodePatch(
    name = "Colorize comment indent lines",
    description = "Replaces the default gray comment indent lines with color-coded lines."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    extendWith("extensions/reddit/frontpage.mpe")

    dependsOn(spoofCertificateHashPatch)

    val isCurrentLevelOnly by booleanOption(
        key = "isCurrentLevelOnly",
        title = "Show indent line at current depth only",
        default = false
    )

    val indentLineColors =
        presetColors.mapIndexed { index, colors ->
            val depth = index + 1

            colorOption(
                key = "indentLineColors$depth",
                default = colors.values.first(),
                values = colors,
                title = "Line color $depth",
                description = "The hex color (#RRGGBB or #AARRGGBB) " +
                    "for comment indent line at depth $depth."
            ) { color ->
                val colorRegex = """^#?([0-9a-fA-F]{6}|[0-9a-fA-F]{8})$""".toRegex()
                !color.isNullOrEmpty() && color.matches(colorRegex)
            }
        }

    execute {
        CommentIndentColorPatchFingerprint.method.apply {
            val arraySizeIndex =
                CommentIndentColorPatchFingerprint.instructionMatches.first().index

            val colorsIndex =
                CommentIndentColorPatchFingerprint.instructionMatches.last().index
            val colorsInstructions =
                getInstruction<BuilderInstruction31t>(colorsIndex)

            val lineColors = indentLineColors.map { it.value!! }
            val lineColorsSize = indentLineColors.size
            val colors =
                lineColors.map { color ->
                    val hex = color.trim().removePrefix("#")
                    val argb = if (hex.length == 6) "FF$hex" else hex
                    argb.toLong(16)
                }

            val arrayPayloadIndex = colorsInstructions.target.location.index
            val arrayPayloadInstruction = getInstruction<BuilderArrayPayload>(arrayPayloadIndex)
            val lineColorsArrayPayload =
                BuilderArrayPayload(
                    arrayPayloadInstruction.elementWidth,
                    colors
                )

            replaceInstruction(
                index = arraySizeIndex,
                smaliInstruction = "const/16 v0, $lineColorsSize"
            )
            replaceInstruction(
                index = arrayPayloadIndex,
                instruction = lineColorsArrayPayload
            )
        }

        CommentIndentDrawInvokeFingerprint.method.apply {
            val extensionClass = CommentIndentColorPatchFingerprint.definingClass
            val drawLineIndex =
                CommentIndentDrawInvokeFingerprint.instructionMatches.last().index

            val alphaIndex =
                CommentIndentAlphaInvokeFingerprint.instructionMatches.last().index
            val alphaRegister =
                CommentIndentAlphaInvokeFingerprint.method
                    .getInstruction<TwoRegisterInstruction>(alphaIndex)
                    .registerA

            addInstruction(
                index = drawLineIndex,
                smaliInstructions = "const/high16 v$alphaRegister, 0x3f800000"
            )

            val strokeCapButtIndex =
                CommentIndentDrawInvokeFingerprint.instructionMatches.first().index

            val colorRegister =
                CommentIndentDrawInvokeFingerprint.method
                    .getInstruction<TwoRegisterInstruction>(strokeCapButtIndex - 1)
                    .registerA

            val depthIndex =
                CommentIndentAlphaInvokeFingerprint.instructionMatches.first().index
            val depthRegister =
                CommentIndentAlphaInvokeFingerprint.method
                    .getInstruction<TwoRegisterInstruction>(depthIndex)
                    .registerA

            addInstructions(
                index = strokeCapButtIndex,
                smaliInstructions = """
                    move v$colorRegister, v$depthRegister
                    invoke-static {v$colorRegister}, $extensionClass->getColorForDepth(I)J
                    move-result-wide v$colorRegister
                """
            )

            if (isCurrentLevelOnly!!) {
                val indentLoopInitIndex =
                    CommentIndentLoopInitFingerprint.instructionMatches.first().index
                val depthValueRegister =
                    CommentIndentAlphaInvokeFingerprint.method
                        .getInstruction<TwoRegisterInstruction>(indentLoopInitIndex)
                        .registerA

                // Change loop start from 1 (draw all depths) to current depth (draw only one)
                replaceInstruction(
                    index = depthIndex,
                    smaliInstruction = "move v$depthRegister, v$depthValueRegister"
                )

                val strokeWidthIndex =
                    CommentIndentStrokeWidthFingerprint.instructionMatches.last().index
                val strokeWidthRegister =
                    CommentIndentStrokeWidthFingerprint.method
                        .getInstruction<OneRegisterInstruction>(strokeWidthIndex)
                        .registerA

                // Increase indent line stroke width from 1dp to 2dp
                replaceInstruction(
                    index = strokeWidthIndex,
                    smaliInstruction = "const/high16 v$strokeWidthRegister, 0x40000000"
                )
            }
        }
    }
}
