package uberpookie.compassion

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resources.Identifier
import com.terraformersmc.modmenu.api.*

@Environment(EnvType.CLIENT)
object CompassionHUDClient : ClientModInitializer, ModMenuApi {
    override fun onInitializeClient() {
        CompassionHUDConfig.load()
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("compassion", "compass_hud"),
            Compass::render
        )

        ServerLifecycleEvents.SERVER_STOPPING.register { _ ->
            CompassionHUDConfig.save(CompassionHUDConfig.instance)
        }
    }
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent -> CompassionHUDConfigScreen.create(parent) }
    }
}