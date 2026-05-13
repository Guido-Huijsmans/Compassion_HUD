package uberpookie.compassion

import net.minecraft.client.gui.*
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class WaypointManageScreen(
    private val parent: Screen?) : Screen(Component.translatable("compassion-hud.manage-waypoints")) {
    private val pageSize = 8
    private var page = 0
    private val pageCount get() = maxOf(1, (WaypointManager.waypoints.size + pageSize -1) / pageSize)
    override fun init() {
        rebuildButtons()
    }
    private fun rebuildButtons() {
        clearWidgets()
        val cx = width / 2
        val startY = 40
        val rowHeight = 24
        val pageWaypoints = WaypointManager.waypoints
            .drop(page * pageSize)
            .take(pageSize)
        for ((i, waypoint) in pageWaypoints.withIndex()) {
            val rowY = startY + i * rowHeight
            addRenderableWidget(Button.builder(Component.literal(if (waypoint.enabled) "✓" else "✗")) {
                WaypointManager.toggleWaypoint(waypoint.id)
                rebuildButtons()
            }.bounds(cx + 60, rowY, 30, 18).build())
        }
        if (page > 0) {
            addRenderableWidget(
                Button.builder(Component.literal("<<")) {
                    page--; rebuildButtons()
                }.bounds(cx - 30, height - 50, 20, 18)
                    .build()
            )
        }
        if (page < pageCount - 1) {
            addRenderableWidget(Button.builder(Component.literal(">>")) {
                page++; rebuildButtons()
            }.bounds(cx - 30, height - 50, 20, 18)
                .build()
            )
        }
        addRenderableWidget(Button.builder(Component.literal("Add Waypoint")) {
            val player = minecraft!!.player ?: return@builder
            val dimension = minecraft!!.level?.dimension()?.identifier()?.toString() ?: "minecraft:overworld"
            minecraft!!.setScreen(WaypointAddScreen(this, player.blockX, player.blockY, player.blockZ, dimension))
        }.bounds(cx - 60, height - 28, 120, 20)
            .build()
        )
    }
    fun render(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        val cx = width / 2
        graphics.centeredText(font, "Waypoints", cx, 14, 0xFFFFFF)
        val startY = 40
        val rowHeight = 24
        val pageWaypoints = WaypointManager.waypoints.drop(page * pageSize).take(pageSize)
        if (pageWaypoints.isEmpty()) {
            graphics.centeredText(font, "No waypoints yet.", cx, height / 2, 0xAAAAAA)
        }
        for ((i, waypoint) in pageWaypoints.withIndex()) {
            val rowY = startY + i * rowHeight

            // Colored swatch
            graphics.fill(cx - 114, rowY + 1, cx - 96, rowY + 17, waypoint.color)
            val abbr = waypoint.abbreviation.take(3)
            val abbrW = font.width(abbr)
            graphics.text(font, abbr, cx - 114 + (18 - abbrW) / 2, rowY + 5, 0xFFFFFF)

            // Name and coordinates
            val label = "${waypoint.name}  §7(${waypoint.x}, ${waypoint.y}, ${waypoint.z})"
            graphics.text(font, label, cx - 90, rowY + 5, if (waypoint.enabled) 0xFFFFFF else 0x888888)
        }

        // Page indicator
        if (pageCount > 1) {
            graphics.centeredText(font, "${page + 1} / $pageCount", cx, height - 47, 0xAAAAAA)
        }
        render(graphics, mouseX, mouseY, partialTick)
    }
    override fun isPauseScreen() = false
}
