package drawings.art

import koan.*
import kotlin.math.*

class Garble : Drawing(800, 800) {

	// The lateinit keyword lets you declare a variable without
	// initializing it. It's a promise to Kotlin that you'll
	// do so later, before using it. If you break your promise you
	// get an exception.
	lateinit var nodes: List<Point>

	// Gaussian is a function that builds a Gaussian function for you!
	// You get back a function with the parameters baked in, so you don't
	// have to pass them in every time.
	val fieldStrength = Gaussian(centre = 0.0, deviation = 75.0)

	override fun reset() {
		clear(rgb(.95, .9, .8))

		// Some texture for the background.
		color(rgb(.5, .3, .2))
		fun deviate(y: Int) = y + rand(-1, 1) * 5
		for (y in 0..canvas.height) {
			opacity(rand(.1))
			line(Point(0, deviate(y)), Point(canvas.width, deviate(y)))
		}

		nodes = List(50) { rand(canvas.size * 2) - canvas.centre }

		// Dark green ink for the patterns to be drawn.
		color(rgb(.05, .15, .05))
	}

	// You'll probably need to draw multiple times to get
	// a solid image (press space on the canvas!).
	override fun draw() {

		val radius = 1

		opacity(.1)
		repeat(10000) {

			// Start with a random point.
			val p = rand(canvas.size)
			if (shouldDraw(p)) {
				fillCircle(p, radius)

				// Once we've found a point that should be drawn,
				// it's likely that nearby points also should be,
				// so we sample some of them as well. This helps
				// to converge the image faster.
				for (i in 1..30) {
					val p2 = p + angleToPoint(rand(TAU)) * rand().pow(.5) * 30
					if (shouldDraw(p2)) fillCircle(p2, radius)
				}

			}
		}

	}

	private fun shouldDraw(p: Point): Boolean {

		// Try playing with this function for different
		// effects. For example, what happens if you add
		// (sin(dist) * .01) to the result of the call
		// to the fieldStrength function?
		val q = nodes.map {
			val dist = p distanceTo it
			fieldStrength(dist)
		}.sum() * 10

		return q % 3 < 1
	}
}
