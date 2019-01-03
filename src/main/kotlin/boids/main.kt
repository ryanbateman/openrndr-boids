import org.openrndr.KEY_SPACEBAR
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.ffmpeg.ScreenRecorder

fun main() = application {

	class Flock {
		val boids = Array(100) { Boid() }
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

				separate = separate.times(1.5)
				align = align.times(1.0)

				boid.applyForce(separate)
				boid.applyForce(align)

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

	configure {
        width = 640
        height = 480
    }

    program {

		// extend(ScreenRecorder())

		val theFlock = Flock()

		keyboard.keyDown.listen {
			if (it.key == KEY_SPACEBAR) {
				theFlock.suspendRule1()
			}
		}

		extend {
			drawer.background(ColorRGBa.WHITE)
			drawer.fill = ColorRGBa.PINK
			drawer.stroke = ColorRGBa.BLACK
			drawer.strokeWeight = 0.5
			theFlock.run(drawer)
		}
    }
}