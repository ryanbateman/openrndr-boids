package boids

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Vector2

object Floor {

	private var count: Double = 0.0
	private var positionX = 640.0 / 2.0
	private var positionY = 480.0 / 2.0
	var currentForce  = 0.0

	fun run(drawer: Drawer) {
		currentForce = Perlin.noise(0.0, 0.0, count) * 10
		if (currentForce < 0)
			drawer.fill = ColorRGBa.RED
		else
			drawer.fill = ColorRGBa.GREEN
		drawer.circle(positionX, positionY, currentForce * 5)
		count += 0.005
	}

	fun position(): Vector2 {
		positionX = 320 - (320 * Perlin.noise(0.0, count + 0.1, 0.0))
		positionY = 240 - (240 * Perlin.noise(count - 0.5, 0.0, 0.0))
		return Vector2(positionX, positionY)
	}

}