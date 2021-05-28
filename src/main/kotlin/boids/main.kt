import boids.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import org.openrndr.KEY_SPACEBAR
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.loadFont
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.launch
import java.net.HttpURLConnection
import java.net.URL

fun URL.getText(): String {
	return openConnection().run {
		this as HttpURLConnection
		inputStream.bufferedReader().readText()
	}
}

fun main() = application {
	configure {
        width = 640
        height = 640
    }

    program {
		val font = loadFont("file:data/FiraCode-Medium.ttf", 15.0)

		val theFlock = Flock()
		var aircraftList: AircraftList? = AircraftList()
		val aircraftListAdapter: JsonAdapter<AircraftList> = setupAircraftParser()
		setupKeyboard(theFlock)
		var once = false;

		extend {
			drawer.fontMap = font

			launch {
				if (!once) {
					once = true
					aircraftList =
					aircraftListAdapter.fromJson(URL("http://192.168.178.19:8080/data/aircraft.json").getText())
					Others.update(aircraftList)
					delay(2000)
					once = false
				}
			}
			drawer.clear(ColorRGBa.WHITE)
			Others.run(drawer)
			theFlock.run(drawer)
		}
    }
}

private fun Program.setupKeyboard(theFlock: Flock) {
	keyboard.keyDown.listen {
		if (it.key == KEY_SPACEBAR) {
			theFlock.suspendRule1()
		}
	}
}

private fun setupAircraftParser(): JsonAdapter<AircraftList> {
	val moshi = Moshi.Builder()
		.addLast(KotlinJsonAdapterFactory())
		.build()
	return moshi.adapter(AircraftList::class.java)
}