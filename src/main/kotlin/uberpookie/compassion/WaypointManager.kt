package uberpookie.compassion

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader

object WaypointManager {
    private val file get() =
        FabricLoader.getInstance().configDir.resolve("compassion-waypoints.json").toFile()
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    val waypoints = mutableListOf<Waypoint>()
    fun load() {
        runCatching {
            if (file.exists()) {
                val loaded = json.decodeFromString<List<Waypoint>>(file.readText())
                waypoints.clear()
                waypoints.addAll(loaded)
            }

        } .onFailure { println("[Compassion HUD]: Failed to load waypoints: ${it.message}")}
    }
    fun save() {
        runCatching { json.encodeToString(kotlinx.serialization.builtins.ListSerializer(Waypoint.serializer()), waypoints)
        } .onFailure { println("[Compassion HUD]: Failed to save waypoints: ${it.message}") }
    }
    fun addWaypoint(waypoint: Waypoint) { waypoints.add(waypoint); save() }
    fun removeWaypoint(id: String) { waypoints.removeIf {it.id == id }; save() }
    fun toggleWaypoint(id: String) { waypoints.find { it.id == id }?.let { it.enabled = !it.enabled; save() } }
}