package uberpookie.compassion

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader

@Serializable
data class CompassionHUDConfig (
    val barWidth: Int = 200,
    val textColor: Int = 0xFFFFFFFF.toInt(),
    val degreesShown: Float = 90f,
    val hideOnDebug: Boolean = true,
    val tickStep: Float = 11.25f,
) {
    companion object {
        private val file get() =
            FabricLoader.getInstance().configDir.resolve("compassion_hud.json").toFile()

        private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

        var instance: CompassionHUDConfig = load()
            private set

        fun load(): CompassionHUDConfig {
            return if (file.exists())
                runCatching { json.decodeFromString<CompassionHUDConfig>(file.readText()) }
                    .getOrDefault(CompassionHUDConfig())
            else CompassionHUDConfig()
        }

        fun save(config: CompassionHUDConfig) {
            instance = config
            file.writeText(json.encodeToString(serializer(), config))
        }
    }
}
