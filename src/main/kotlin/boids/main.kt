import boids.AircraftList
import boids.Flock
import boids.Floor
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.openrndr.KEY_SPACEBAR
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.ffmpeg.ScreenRecorder
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
        height = 480
    }

    program {
		val theFlock = Flock()
		keyboard.keyDown.listen {
			if (it.key == KEY_SPACEBAR) {
				theFlock.suspendRule1()
			}
		}

		val moshi = Moshi.Builder()
			.addLast(KotlinJsonAdapterFactory())
			.build()
		val aircraftListAdapter: JsonAdapter<AircraftList> = moshi.adapter(AircraftList::class.java)
		val airCraftList = aircraftListAdapter.fromJson(URL("http://192.168.178.19:8080/data/aircraft.json").getText())
		extend {
			GlobalScope.launch {

			}
			drawer.background(ColorRGBa.WHITE)
			Floor.run(drawer)
			theFlock.run(drawer)
		}
    }

}