package boids

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AircraftList (
    var now: Float,
    var aircraft: List<Aircraft>
)
