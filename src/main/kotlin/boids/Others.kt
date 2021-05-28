package boids

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2
import org.openrndr.shape.contour

object Others {
	private var aircraftToDraw: List<Aircraft> = arrayListOf<Aircraft>()
	private var count: Double = 0.0
	var currentForce  = 0.0

	private val bodyContour = contour {
		val aircraftRadius = 2.0
		moveTo(Vector2(0.0, -aircraftRadius * 2.0))
		lineTo(Vector2(-aircraftRadius, aircraftRadius * 2))
		lineTo(Vector2(aircraftRadius, aircraftRadius * 2))
		lineTo(anchor)
		close()
	}

	private fun convertLatLonToPosition(height:Int, width:Int, latLonToConvert:LatLon): Vector2 {
		val berlinBounds = boundingBox(LatLon(51.285863, 11.286435), LatLon(53.510816, 15.230527))
		var positionY = height - (height * (latLonToConvert.lat - berlinBounds.startLatLon.lat) / (berlinBounds.endLatLon.lat - berlinBounds.startLatLon.lat))
		var positionX =
			width * (latLonToConvert.lon - berlinBounds.startLatLon.lon) / (berlinBounds.endLatLon.lon - berlinBounds.startLatLon.lon)
		return Vector2(positionX, positionY)
	}

	fun run(drawer: Drawer) {
		for (aircraft in aircraftToDraw) {
			drawer.stroke = ColorRGBa.TRANSPARENT
			currentForce = Perlin.noise(0.0, 0.0, count) * 25
			val drawerCoordinates = convertLatLonToPosition(drawer.height, drawer.width, LatLon(aircraft.lat!!, aircraft.lon!!))
			//drawer.circle(drawerCoordinates.first, drawerCoordinates.second, currentForce * 5)
			if (currentForce < 0) {
				drawer.fill = ColorRGBa.RED
			} else {
				drawer.fill = ColorRGBa.GREEN
			}
			aircraft.mag_heading?.let {
				drawer.translate(drawerCoordinates.x, drawerCoordinates.y)
				drawer.rotate(it)
				drawer.contour(bodyContour)
				aircraft.mag_heading?.let { drawer.rotate(-it) }
				drawer.translate(-drawerCoordinates.x, -drawerCoordinates.y)
			} ?: drawer.circle(drawerCoordinates.x, drawerCoordinates.y, 5.0)

			drawer.stroke = ColorRGBa(0.0, 0.5, 0.0, 1.0)
			drawer.fill = ColorRGBa.BLACK
			drawer.text(aircraft.flight ?: aircraft.hex, drawerCoordinates.x + 2.0, drawerCoordinates.y)

			// Iterate noise
			count += 0.0005
		}
	}

	fun relevantPositions() : List<Vector2> {
		return aircraftToDraw.map { convertLatLonToPosition(640, 640, LatLon(it.lat!!, it.lon!!)) }
	}

	fun update(aircraftList: AircraftList?) {
		aircraftToDraw = aircraftList!!.aircraft.filter { it.lat != null && it.lon != null}
	}
}