package uberpookie.compassion

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resources.Identifier
import com.terraformersmc.modmenu.api.*
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.KeyMapping.Category
import org.lwjgl.glfw.GLFW
import net.minecraft.network.chat.Component

@Environment(EnvType.CLIENT)
object CompassionHUDClient : ClientModInitializer, ModMenuApi {
    private val CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("compassion-hud", "custom-category"))
    private lateinit var waypointAddKeymapping: KeyMapping
    private lateinit var waypointManageKeymapping: KeyMapping
    override fun onInitializeClient() {
        CompassionHUDConfig.load()
        WaypointManager.load()
        waypointAddKeymapping =  KeyMappingHelper.registerKeyMapping(KeyMapping("key.compassion_hud.waypoint-add", GLFW.GLFW_KEY_B, CATEGORY))
        waypointManageKeymapping = KeyMappingHelper.registerKeyMapping(KeyMapping("key.compassion_hud.waypoint-manage", GLFW.GLFW_KEY_B, CATEGORY))
        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (waypointAddKeymapping.consumeClick()) {
                val player = client.player ?: return@register
                val dimension = client.level?.dimension().toString()
                client.setScreen(WaypointAddScreen(null, player.blockX, player.blockY, player.blockZ, dimension))
            }
            if (waypointManageKeymapping.consumeClick()) {
                client.setScreen(WaypointManageScreen(null))
            }
        }
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("compassion", "compass_hud"),
            Compass::render
        )
        // Save config when server stops
        ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
            CompassionHUDConfig.save(CompassionHUDConfig.instance)
        }
    }
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent -> CompassionHUDConfigScreen.create(parent) }
    }
}