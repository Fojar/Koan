package drawings.techniques

import koan.*
import kotlin.math.*

class Circle_packing : Drawing(500, 500) {

    // Radius of the black outer circle.
    val OUTER_RADIUS = 200

    // Maximum radius of the inner circles.
    val MAX_RADIUS = OUTER_RADIUS / 2

    // Used to remember the position and size of a circle, as well as
    // to calculate whether 2 circles overlap.
    data class Circle(val centre: Point, var radius: Double) {
        fun overlaps(other: Circle): Boolean {
            val radiiSum = radius + other.radius
            val distance = centre distanceTo other.centre
            return radiiSum >= distance
        }
    }


    // To keep track of all the circles as they are drawn.
    val circles = mutableListOf<Circle>()

    // Determines whether a circle overlaps the outer circle.
    fun Circle.hitsOuterCircle() = this.centre.distanceTo(canvas.centre) + radius >= OUTER_RADIUS

    // Determiens whether a circle cannot be placed because it would overlap another circle.
    fun Circle.isUnplacable() = hitsOuterCircle() || circles.any { overlaps(it) }


    override fun draw() {

        color(hsv(rand(), .5, .75))
        drawOrb(.3)

        color(hsv(rand(), .5, .75))
        drawOrb(.7)

        opacity(1)
        color(BLACK)
        stroke(3)
        drawCircle(canvas.centre, OUTER_RADIUS)
    }

    private fun drawOrb(opacity: Double) {
        circles.clear()
        opacity(opacity)

        repeat(1000) {
            growCircle()
        }
    }

    // Attempts to spawn a new tiny circle at a random location, and then expand it
    // until it either touches another circle or reaches maximum size.
    fun growCircle() {

        val c = seedCircle() ?: return

        while (c.radius < MAX_RADIUS) {
            c.radius += .5

            if (c.isUnplacable()) {
                c.radius *= .9
                break
            }
        }

        circles.add(c)
        stroke(c.radius.pow(.15))
        drawCircle(c.centre, c.radius)
    }

    // This to find a spot to place a new tiny circle.
    // If no such spot can be found, returns null.
    fun seedCircle(): Circle? {

        for (tries in 1..100) {
            val c = Circle(rand(canvas.size), 2.0)
            if (!c.isUnplacable()) return c
        }
        return null
    }
}
