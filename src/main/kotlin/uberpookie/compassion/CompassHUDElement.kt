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
    private const val COLOR = 0xFFDDDDDD.toInt()

    private val directions = listOf(
        "compassion-hud.north" to 180f,
        "compassion-hud.northwest" to 135f,
        "compassion-hud.west" to 90f,
        "compassion-hud.southwest" to 45f,
        "compassion-hud.south" to 0f,
        "compassion-hud.southeast" to -135f,
        "compassion-hud.east" to -90f,
        "compassion-hud.northeast" to -45f

    )

    private val LABEL_ANGLES = setOf(
        0f, 45f, 90f, 135f, 180f, -135f, -90f, -45f, -180f
    )


    fun render(graphics: GuiGraphicsExtractor, delta: DeltaTracker) {
        val mc = Minecraft.getInstance()
        val player = mc.player ?: return
        if (mc.options.hideGui) return

        val config = CompassionHUDConfig.instance
        if (config.hideOnDebug && mc.debugOverlay.showDebugScreen()) return

        val barWidth = config.barWidth
        val textColor = config.textColor
        val degreesShown = config.degreesShown
        val tickStep = config.tickStep

        val screenWidth = mc.window.guiScaledWidth
        val barX = (screenWidth - barWidth) / 2
        val barY = 4
        val centerX = barX + barWidth / 2
        val pixelsPerDegree = barWidth / degreesShown

        val yaw = Mth.wrapDegrees(player.yRot)
        val font = mc.font

        for ((key, dirYaw) in directions) {
            val label = Component.translatable(key).string
            val offset = Mth.wrapDegrees(dirYaw - yaw)
            if (offset < -(degreesShown / 2 + 10) || offset > (degreesShown / 2 + 10)) continue
            val labelX = (centerX + offset * pixelsPerDegree).toInt()
            val textWidth = font.width(label)
            graphics.text(font, label, labelX - textWidth / 2, barY + 4, textColor)
        }

        val directionTicks = generateSequence(-180f) { it + tickStep }
            .takeWhile { it <= 180f }
            .filterNot { angle -> LABEL_ANGLES.any { kotlin.math.abs(it - angle) < 0.01f } }
            .map { "|" to it }
            .toList()

        for ((key, dirYaw) in directionTicks) {
            val label = Component.translatable(key).string
            val offset = Mth.wrapDegrees(dirYaw - yaw)
            if (offset < -(degreesShown / 2 + 10) || offset > (degreesShown / 2 + 10)) continue
            val labelX = (centerX + offset * pixelsPerDegree).toInt()
            val textWidth = font.width(label)
            graphics.text(font, label, labelX - textWidth / 2, barY + 4, textColor)
        }
    }
}
