package uberpookie.compassion

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.resources.Identifier

@Environment(EnvType.CLIENT)
object CompassionHUDClient : ClientModInitializer {
    override fun onInitializeClient() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.fromNamespaceAndPath("compassion", "compass_hud"),
            CompassHUDElement::render
        )
    }
}