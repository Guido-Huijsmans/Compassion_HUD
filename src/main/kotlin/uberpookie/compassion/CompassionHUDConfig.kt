package uberpookie.compassion

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.io.File

@Serializable
data class CompassionHUDConfig(
    val barWidth:       Int = 200,
    val textColor:      Int = 0xFFFFFFFF.toInt(),
    val degreesShown:   Float = 90f,
    val hideOnDebug:    Boolean = true,
    val tickStep:       Float = 11.25f,
    val coordsPosition: CoordinatePosition = CoordinatePosition.CORNER_LEFT) {

    companion object {
        private val json = Json { 
            prettyPrint = true
            ignoreUnknownKeys = true 
        }
        
        private val configFile: File
            get() = FabricLoader.getInstance().configDir.resolve("compassion_hud.json").toFile()

        var instance: CompassionHUDConfig = CompassionHUDConfig()
            private set

        fun load() {
            try {
                if (configFile.exists()) {
                    instance = json.decodeFromString<CompassionHUDConfig>(configFile.readText())
                    println("[Compassion HUD]: Config loaded from ${configFile.absolutePath}")
                } else {
                    instance = CompassionHUDConfig()
                    println("[Compassion HUD]: No config file found, using defaults")
                }

            } catch (e: Exception) {
                println("[Compassion HUD]: Failed to load config: ${e.message}")
                e.printStackTrace()
                instance = CompassionHUDConfig()
            }
        }

        fun save(config: CompassionHUDConfig) {
            try {
                instance = config
                configFile.parentFile?.mkdirs()
                configFile.writeText(json.encodeToString(config))
                println("[Compassion HUD]: Config saved to ${configFile.absolutePath}")
            } catch (e: Exception) {
                println("[Compassion HUD]: Failed to save config: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
