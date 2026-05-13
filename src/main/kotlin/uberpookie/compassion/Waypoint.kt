package uberpookie.compassion

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Waypoint(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val abbreviation: String,
    val color: Int,
    val x: Int,
    val y: Int,
    val z: Int,
    val dimension: String,
    var enabled: Boolean = true
)