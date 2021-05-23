package boids

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AircraftList (
    var now: Float = 0.0f,
    var aircraft: List<Aircraft> = ArrayList<Aircraft>()
) {
    constructor() : this(0.0f, emptyList())
}
