package boids

import convertLatLonToPosition
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

object Others {
	private var aircraftToDraw: List<Aircraft> = arrayListOf<Aircraft>()
	private var count: Double = 0.0
	var currentForce  = 0.0

	fun run(drawer: Drawer) {
		for (aircraft in aircraftToDraw) {
			currentForce = Perlin.noise(0.0, 0.0, count) * 10
			val drawerCoordinates = drawer.convertLatLonToPosition(LatLon(aircraft.lat!!, aircraft.lon!!))
			//drawer.circle(drawerCoordinates.first, drawerCoordinates.second, currentForce * 5)
			if (currentForce < 0) {
				drawer.stroke = ColorRGBa(0.5, 0.0, 0.0, 1.0)
				drawer.fill = ColorRGBa.RED
			} else {
				drawer.stroke = ColorRGBa(0.0, 0.5, 0.0, 1.0)
				drawer.fill = ColorRGBa.GREEN
			}
			drawer.circle(drawerCoordinates.first, drawerCoordinates.second, 15.0)

			drawer.fill = ColorRGBa.BLACK
			drawer.text(aircraft.hex, drawerCoordinates.first, drawerCoordinates.second)

			// Iterate noise
			count += 0.005
		}
	}

	fun position(){

	}

	fun update(aircraftList: AircraftList?) {
		aircraftToDraw = aircraftList!!.aircraft.filter { it.lat != null && it.lon != null}
	}
}