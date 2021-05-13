package boids

import org.openrndr.draw.Drawer
import Boid

class Flock {
	val boids = Array(5) { Boid() }
	var rule1 = true

	fun suspendRule1() {
		rule1 = !rule1
	}

	fun run(drawer: Drawer) {
		flock()
		update()
		render(drawer)
	}

	fun flock() {
		for (boid in boids) {
			var separate = boid.separate(boids)
			var align = boid.align(boids)
			var cohesion = boid.cohesion(boids)
			var dodge = boid.avoid(Floor.position())

			dodge = dodge.times(Floor.currentForce)
			separate = separate.times(1.5)
			align = align.times(1.0)

			boid.applyForce(separate)
			boid.applyForce(align)
			boid.applyForce(dodge)

			if (rule1) {
				cohesion = cohesion.times(1.0)
				boid.applyForce(cohesion)
			} else {
				cohesion = cohesion.times(3.0)
				boid.applyForce(cohesion.times(-1.0))
			}
		}
	}

	fun render(drawer: Drawer) {
		for (boid in boids) {
			boid.render(drawer)
		}
	}

	fun update() {
		for (boid in boids) {
			boid.update()
			boid.borders()
		}
	}

}