import boids.Flock
import boids.Floor
import org.openrndr.KEY_SPACEBAR
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.ffmpeg.ScreenRecorder

fun main() = application {

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
			drawer.stroke = ColorRGBa.BLACK
			drawer.strokeWeight = 0.5
			Floor.run(drawer)
			drawer.fill = ColorRGBa.PINK
			theFlock.run(drawer)
		}
    }
}