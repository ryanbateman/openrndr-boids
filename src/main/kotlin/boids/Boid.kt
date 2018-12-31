import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import kotlin.random.Random

class Boid {

	var position: Vector3
	var velocity: Vector3
	var acceleration: Vector3

	val maxForce: Double = 0.4
	val maxSpeed: Double = 6.0


	fun Vector3.limit(max: Double) : Vector3 {
		var limited = Vector3(x, y, z)
		if (length > max) {
			limited = this.normalized.times(max)
		}
		return limited
	}

	fun Vector3.distance(other: Vector3) : Double {
		val dx = x - other.x
		val dy = y - other.y
		val dz = z - other.z

		return Math.sqrt(dx * dx + dy * dy + dz * dz)
	}

	init {
		position = Vector3(
			Random.nextDouble(800.0),
			Random.nextDouble(800.0),
			Random.nextDouble(800.0)
		)
		velocity = Vector3(
			Random.nextDouble(-maxSpeed, maxSpeed),
			Random.nextDouble(-maxSpeed, maxSpeed),
			Random.nextDouble(-maxSpeed, maxSpeed))
		acceleration = Vector3(
			Random.nextDouble(-1.0, 1.0),
			Random.nextDouble(-1.0, 1.0),
			Random.nextDouble(-1.0, 1.0)
		)
	}

	fun separate(boids: Array<Boid>) : Vector3 {
		val desiredDistance = 40.0
		var steer = Vector3(0.0, 0.0, 0.0)
		var count = 0.0

		for (other in boids) {
			val distance = position.distance(other.position)
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((distance > 0) && (distance < desiredDistance)) {
				// Calculate vector pointing away from neighbor
				var diff = position.minus(other.position);
				diff = diff.normalized
				diff = diff.div(distance)
				steer = steer.plus(diff)
				count++
			}
		}

		// Average out steer by number affecting
		if (count > 0) {
			steer = steer.div(count)
		}

		if (steer.length > 0) {
			steer = steer.normalized
			steer = steer.times(maxSpeed)
			steer = steer.minus(velocity)
			steer = steer.limit(maxForce)
		}

		return steer
	}

	fun align(boids: Array<Boid>): Vector3 {
		val neighourDistance = 100.0
		var sum = Vector3(0.0, 0.0, 0.0)
		var count = 0.0

		for (other in boids) {
			val d = position.distance(other.position)
			if (d > 0 && d < neighourDistance) {
				sum = sum.plus(other.velocity)
				count++
			}
		}

		if (count > 0) {
			sum = sum.div(count)
			sum = sum.normalized
			sum = sum.times(maxSpeed)
			var steer = sum.minus(velocity)
			steer = steer.limit(maxForce)
			return steer
		} else {
			return Vector3(0.0, 0.0, 0.0)
		}
	}

	fun cohesion(boids: Array<Boid>): Vector3 {
		val neighbordist = 100.0
		var sum = Vector3(0.0, 0.0, 0.0)   // Start with empty vector to accumulate all positions
		var count = 0.0
		for (other in boids) {
			val d = position.distance(other.position)
			if (d > 0 && d < neighbordist) {
				sum = sum.plus(other.position)
				count++
			}
		}
		if (count > 0) {
			sum = sum.div(count)
			return seek(sum)
		} else {
			return Vector3(0.0, 0.0, 0.0)
		}
	}

	fun seek(target: Vector3): Vector3 {
		var desired = target.minus(position)  // A vector pointing from the position to the target
		// Scale to maximum speed
		desired = desired.normalized
		desired = desired.times(maxSpeed)

		// Steering = Desired minus Velocity
		var steer = desired.minus(velocity)
		steer = steer.limit(maxForce)
		return steer
	}

	fun applyForce(force: Vector3) {
		acceleration = acceleration.plus(force)
	}

	fun update() {
		velocity = velocity.plus(acceleration)
		velocity = velocity.limit(maxSpeed)
		position = position.plus(velocity)
		acceleration = acceleration.times(0.0)

		borders()
	}

	fun borders() {

		var x = position.x
		var y = position.y
		var z = position.z

		if (position.x < -5) x = 800.0 + 5.0;
		if (position.y < -5) y = 800.0 + 5.0;
		if (position.z < -5) z = 800.0 + 5.0;
		if (position.x > 800+ 5) x = -5.0;
		if (position.y > 800 + 5) y = -5.0;
		if (position.z > 800 + 5) z = -5.0;

		position = Vector3(x, y, z)
	}
}