package uberpookie.compassion

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.util.Mth

object CompassHUDElement {

    private const val BAR_WIDTH = 192
    private const val BAR_HEIGHT = 16
    private const val DEGREES_SHOWN = 90f
    private const val BG_COLOR = 0x88000000.toInt()
    private const val CARDINAL_COLOR = 0xFFFFAA00.toInt()
    private const val ORDINAL_COLOR = 0xFFCCCCCC.toInt()
    private const val TICK_COLOR = 0xFFFFFFFF.toInt()

    private val directions = listOf(
        "S" to 0f,
        "SW" to 45f,
        "W" to 90f,
        "NW" to 135f,
        "N" to 180f,
        "NE" to -135f,
        "E" to -90f,
        "SE" to -45f,
    )

    fun render(graphics: GuiGraphicsExtractor, delta: DeltaTracker) {
        val mc = Minecraft.getInstance()
        val player = mc.player ?: return
        if (mc.options.hideGui) return
        val screenWidth = mc.window.guiScaledWidth
        val barX = (screenWidth - BAR_WIDTH) / 2
        val barY = 4
        val centerX = barX + BAR_WIDTH / 2
        val pixelsPerDegree = BAR_WIDTH / DEGREES_SHOWN

        graphics.fill(barX, barY, barX + BAR_WIDTH, barY + BAR_HEIGHT, BG_COLOR)

        val yaw = Mth.wrapDegrees(player.yRot)

        for ((label, dirYaw) in directions) {
            val offset = Mth.wrapDegrees(dirYaw - yaw)
            if (offset < -(DEGREES_SHOWN / 2 + 10) || offset > (DEGREES_SHOWN / 2 + 10)) continue
            val labelX = (centerX + offset * pixelsPerDegree).toInt()
            val color = if (label.length == 1) CARDINAL_COLOR else ORDINAL_COLOR
            val font = mc.font
            val textWidth = font.width(label)
            graphics.text(font, label, labelX - textWidth / 2, barY + 4, color)
        }
    }
}