package uberpookie.compassion

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import com.terraformersmc.modmenu.api.*
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.resources.Identifier

object CompassionHUD : ModInitializer {
    private val logger = LoggerFactory.getLogger("compassion-hud")

	override fun onInitialize() {
		logger.info("Compassion HUD Initialized")
	}
}