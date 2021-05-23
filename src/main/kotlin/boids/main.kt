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
import org.openrndr.launch
import java.net.HttpURLConnection
import java.net.URL

fun URL.getText(): String {
	return openConnection().run {
		this as HttpURLConnection
		inputStream.bufferedReader().readText()
	}
}

fun Drawer.convertLatLonToPosition(latLonToConvert:LatLon): Pair<Double, Double> {
	val berlinBounds = boundingBox(LatLon(51.912853,12.553811), LatLon(53.033704,14.317117))
	var positionX =
		this.width * (latLonToConvert.lat - berlinBounds.startLatLon.lat) / (berlinBounds.endLatLon.lat - berlinBounds.startLatLon.lat)
	var positionY =
		this.height * (latLonToConvert.lon - berlinBounds.startLatLon.lon) / (berlinBounds.endLatLon.lon - berlinBounds.startLatLon.lon)
	return Pair(positionX, positionY)
}


fun main() = application {

	configure {
        width = 640
        height = 640
    }

    program {
		val theFlock = Flock()
		var aircraftList: AircraftList? = AircraftList()
		val aircraftListAdapter: JsonAdapter<AircraftList> = setupAircraftParser()
		setupKeyboard(theFlock)
		var once = false;

		extend {
			launch {
				if (!once) {
					once = true
					aircraftList =
					aircraftListAdapter.fromJson(URL("http://192.168.178.19:8080/data/aircraft.json").getText())
					Others.update(aircraftList)
					delay(5000)
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