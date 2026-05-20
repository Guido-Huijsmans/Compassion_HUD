package uberpookie.compassion

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object CompassionHUD : ModInitializer {
    private val logger = LoggerFactory.getLogger("compassion-hud")

	override fun onInitialize() {
		logger.info("Compassion HUD Initialized")
	}
}