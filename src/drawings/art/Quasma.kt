package drawings.art

import koan.*

class Quasma : Drawing(800, 800) {

	// A class that keeps track of a particle's position,
	// and updates it over time (straight-line motion).
	data class Particle(var pos: Point) {
		val dir = angleToPoint(rand(TAU))

		fun update() {
			// You can try other types of motion here,
			// such as moving in a circle.
			pos += dir
		}
	}

	// The lateinit keyword lets you declare a variable without
	// initializing it. It's a promise to Kotlin that you'll
	// do so later, before using it. If you break your promise you
	// get an exception.
	lateinit var particles: List<Particle>

	override fun reset() {
		clear(BLACK)
		// The additive mode gives the glowing effect. Try commenting
		// out the next line to see how dull regular mode looks!
		blendingMode(BlendingMode.ADDITIVE)
	}

	// Generates a new list of random particles.
	private fun resetParticles(count: Int) {
		particles = List(count) { Particle(rand(canvas.size)) }
	}

	override fun draw() {
		resetParticles(50)
		color(rgb(1, .8, .1))
		drawFilaments(150)

		resetParticles(50)
		color(rgb(.35, .1, 1))
		drawFilaments(150)
	}

	private fun drawFilaments(maxDist: Int) {

		repeat(1200) {

			// The allPairs function is provided by Koan.
			// We'll draw a line between any 2 points that
			// come closer than the max distance parameter.
			allPairs(particles).forEach { (a, b) ->
				val dist = a.pos distanceTo b.pos
				if (dist < maxDist) {
					// Adjust to opacity so as the distance approaches
					// the max, the opacity approaches 0.
					opacity(.1 * (maxDist - dist) / maxDist)
					line(a.pos, b.pos)
				}
			}

			opacity(.3)
			particles.forEach {
				// Uncomment the following line to visualize the path of the particles.
				//fillCircle(it.pos, 1)
				it.update()
			}

		}
	}
}

