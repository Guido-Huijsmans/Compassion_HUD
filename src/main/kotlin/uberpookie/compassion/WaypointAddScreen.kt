package uberpookie.compassion

import com.mojang.authlib.minecraft.client.MinecraftClient
import net.minecraft.client.gui.*
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import org.apache.logging.log4j.core.pattern.TextRenderer

class WaypointAddScreen(
    private val parent: Screen?,
    private val x: Int,
    private val y: Int,
    private val z: Int,
    private val dimension: String
): Screen(Component.translatable("compassion-hud.add-waypoint")) {
    companion object {
        val COLOR_PALETTE = listOf(
            0x000000FF.toInt(),
            0x0000AAFF.toInt(),
            0x00AA00FF.toInt(),
            0x00AAAAFF.toInt(),
            0xAA0000FF.toInt(),
            0xAA00AAFF.toInt(),
            0xFFAA00FF.toInt(),
        )
    }
    private lateinit var nameField: EditBox
    private lateinit var abbrField: EditBox
    private var selectedColor: Int = COLOR_PALETTE[0]
    override fun init() {
        super.init()
        val cx = width / 2
        nameField = EditBox(font, cx - 80, 60, 160, 20, Component.translatable("compassion-hud.waypoint-name"))
        addRenderableWidget(nameField)
        abbrField = EditBox(font, cx - 80, 90, 160, 20, Component.translatable("compassion-hud.waypoint-abbr"))
        addRenderableWidget(abbrField)
        val AddButton: Button = Button.builder(Component.translatable("compassion-hud.add-waypoint")) {
            val name = nameField.toString().trim()
            val abbr = abbrField.toString().trim().uppercase()

            if (name.isNotEmpty() && abbr.isNotEmpty()) {
                WaypointManager.addWaypoint(
                    Waypoint(name = name, abbreviation = abbr, color = selectedColor, x = x, y = y, z = z, dimension = dimension)
                )
                minecraft?.setScreen(parent)
            }
        } .bounds(cx - 80, 120, 75, 20)
            .build()
        addRenderableWidget(AddButton)
        val cancelButton = Button.builder(Component.translatable("gui.cancel")) {
            minecraft?.setScreen(parent)
        } .bounds(cx + 5, 120, 75, 20)
            .build()
        addRenderableWidget(cancelButton)
    }
}