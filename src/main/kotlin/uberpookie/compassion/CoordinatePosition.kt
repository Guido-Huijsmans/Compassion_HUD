package uberpookie.compassion
import kotlinx.serialization.Serializable

@Serializable
enum class CoordinatePosition {
    CORNER_LEFT, CORNER_RIGHT, COMPASS_LEFT, COMPASS_RIGHT, NONE;

    val translationKey get() = "compassion.config.coordinate_position.${name.lowercase()}"
}