package drawings.art

import koan.*

class Chaos_rings : Drawing(500, 500) {

    val POINTS_COUNT = 30
    var points = mutableListOf<Point>()

    override fun reset() {

        points = MutableList<Point>(POINTS_COUNT) {
            rand(canvas.size)
        }

        // Move all the points so that they're centered in the canvas.
        val barycentre = points.fold(Point.ZERO, Point::plus) / POINTS_COUNT
        val offset = canvas.centre - barycentre
        for (i in points.indices) points[i] += offset

        clear()
        opacity(.01)
    }

    override fun draw() {

        repeat(100) {
            points.forEach { fillCircle(it, 1) }

            for ((pIndex, p) in points.withIndex()) {
                for ((qIndex, q) in points.withIndex()) {

                    if (pIndex == qIndex) continue

                    // Each particle will try to maintain a distance of 250
                    // pixels from every other particle. This calculation being
                    // order-dependent produces chaos.
                    val delta = p - q
                    val distance = delta.magnitude
                    val normal = delta / distance
                    val shift = normal * (distance - 250) * .01

                    points[pIndex] -= shift
                    points[qIndex] += shift
                }
            }
        }

        // Without occasional shuffling, the particles will eventually
        // find a stable configuration. Try commenting this out to see!
        if (rand() < .01) points.shuffle()
    }

}
