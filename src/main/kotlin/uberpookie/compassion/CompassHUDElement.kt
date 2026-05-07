package uberpookie.compassion

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth

object CompassHUDElement {

    private const val BAR_WIDTH = 256
    private const val BAR_HEIGHT = 16
    private const val DEGREES_SHOWN = 90f
    private const val COLOR_YELLOW = 0xFFFFAA00.toInt()
    private const val COLOR_WHITE = 0xFFDDDDDD.toInt()

    private val directions = listOf(
        "compassion-hud.north" to 0f,
        "compassion-hud.northwest" to 45f,
        "compassion-hud.west" to 90f,
        "compassion-hud.southwest" to 135f,
        "compassion-hud.south" to 180f,
        "compassion-hud.southeast" to -45f,
        "compassion-hud.east" to -90f,
        "compassion-hud.northeast" to -135f

    )

    private val LABEL_ANGLES = setOf(
        0f, 45f, 90f, 135f, 180f, -135f, -90f, -45f
    )

    private const val TICK_STEP: Float = 5f
    private val directionTicks = generateSequence(-175f) { it + TICK_STEP }
        .takeWhile { it <= 175f }
        .filterNot { angle -> LABEL_ANGLES.any { kotlin.math.abs(it - angle) < 0.01f } }
        .map { "|" to it }
        .toList()

    fun render(graphics: GuiGraphicsExtractor, delta: DeltaTracker) {
        val mc = Minecraft.getInstance()
        val player = mc.player ?: return
        if (mc.options.hideGui) return
        if (mc.debugOverlay.showDebugScreen()) return
        val screenWidth = mc.window.guiScaledWidth
        val barX = (screenWidth - BAR_WIDTH) / 2
        val barY = 4
        val centerX = barX + BAR_WIDTH / 2
        val pixelsPerDegree = BAR_WIDTH / DEGREES_SHOWN
        val yaw = Mth.wrapDegrees(player.yRot)

        for ((key, dirYaw) in directions) {
            val label = Component.translatable(key).string
            val offset = Mth.wrapDegrees(dirYaw - yaw)
            if (offset < -(DEGREES_SHOWN / 2 + 10) || offset > (DEGREES_SHOWN / 2 + 10)) continue
            val labelX = (centerX + offset * pixelsPerDegree).toInt()
            val font = mc.font
            val textWidth = font.width(label)
            graphics.text(font, label, labelX - textWidth / 2, barY + 4, COLOR_WHITE)
        }

        for ((label, dirYaw) in directionTicks) {
            val offset = Mth.wrapDegrees(dirYaw - yaw)
            if (offset < -(DEGREES_SHOWN / 2 + 10) || offset > (DEGREES_SHOWN / 2 + 10)) continue
            val labelX = (centerX + offset * pixelsPerDegree).toInt()
            val font = mc.font
            val textWidth = font.width(label)
            graphics.text(font, label, labelX - textWidth / 2, barY + 4, COLOR_WHITE)
        }
    }
}