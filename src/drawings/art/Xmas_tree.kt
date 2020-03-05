package drawings.art

import koan.*
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import kotlin.math.*


val NEGATIVE_GREEN = hsv(.9, .5, .85)

class Xmas_tree : Drawing(800, 1000) {

    // The tree is made out of circles in the same was as the
    // Circle_packing technique. We just draw them differently!
    data class Circle(var centre: Point, var radius: Double) {

        fun overlaps(other: Circle): Boolean {
            val radiiSum = radius + other.radius
            val distance = centre distanceTo other.centre
            return radiiSum >= distance
        }

        fun drawLeaf(d: Drawing, opacity: Real) = with(d) {

            color(NEGATIVE_GREEN)
            blendingMode(BlendingMode.SUBTRACTIVE)

            stroke(radius.pow(.25))

            // Find a point that is slightly offset from the centre.
            val c = centre + angleToPoint(rand(TAU)) * rand().pow(3) * 5

            // Make a small irregular polygon around that offset centre.
            val count = rint(5) + 3
            val points = List(count) { i ->
                c + angleToPoint(TAU * i / count) * radius + angleToPoint(rand(TAU)) * radius * .3
            }

            // Generate a smoothly curved shape out of that polygon, but then
            // perturb the vertices to add some irregularity. Try commenting
            // out the map line to see the smooth shape.
            val leaf = points.closedSmoothed()
                .map { it -> it + angleToPoint(rand(TAU) * .5) }

            // Draw some faint circles at the centre point to suggest
            // some material connecting the leaves.
            opacity(opacity * .02)
            for (f in 6 downTo 2 step 2) fillCircle(c, radius * f)

            // Draw the actual "leaf" shape more solidly.
            opacity(opacity)
            fillPolygon(leaf)
        }

        fun draw(d: Drawing, opacity: Real) = with(d) {

            // Uncomment the following 2 lines to visualize
            // the circles only:
            // drawCircle(centre, radius)
            // return

            // Small circles are drawn as leaves, while larger
            // circles are empty space with a bauble in the middle.
            if (radius < 7) drawLeaf(d, opacity)
            else if (radius > 20) drawBauble(d, opacity)
        }

        private fun drawBauble(d: Drawing, opacity: Real) = with(d) {

            color(RED)
            blendingMode(BlendingMode.NORMAL)

            // We want a circle that covers the same area as:
            // fillCircle(centre, 10)
            // but with some shading and highlight. The bauble
            // is built up line by line so the colour can be
            // altered on each line.

            // This can be done much simpler using gradients,
            // which is available in Java2D, but I haven't yet
            // exposed all that in Koan.

            for (y in -10..10) {
                val x = sqrt(100.0 - y * y)
                val q = (y + 10) / 20.0
                color(hsv(0, 1, (1..(1 - q)).lerp(.5)))
                line(centre + Point(-x, y), centre + Point(x, y))
            }

            color(WHITE)
            val shift = 4
            for (y in -4..4) {
                val x = sqrt(16.0 - y * y) * 1.5
                val q = (y + 4) / 8.0
                opacity(1 - q)
                line(centre + Point(-x, y - shift), centre + Point(x, y - shift))
            }

        }

    }


    // To keep track of all the circles as they are drawn.
    val circles = mutableListOf<Circle>()


    // Determines whether a circle can't be placed because it would
    // overlap another circle or the given area.
    fun Circle.isUnplacable(area: Area): Boolean {

        // We need to create an area object to get access to the
        // geometric boolean operations.
        val ca = Area(Ellipse2D.Double(centre.x - radius, centre.y - radius, radius * 2, radius * 2))
        ca.intersect(area)

        return !ca.isEmpty || circles.any { overlaps(it) }
    }

    override fun reset() {
        translate(canvas.centre)
        clear()
        blendingMode(BlendingMode.SUBTRACTIVE)
    }

    override fun draw() {
        color(NEGATIVE_GREEN)

        // Define the triangle within which all the circles
        // will be restricted. First we make a rectangle covering
        // the canvas...
        val area = Area(
            Rectangle2D.Double(
                -canvas.halfWidth * 1.0, -canvas.halfHeight * 1.0,
                canvas.width * 1.0, canvas.height * 1.0
            )
        )

        // ...and then we cut out the triangle wherein circles will be allowed.
        area.subtract(
            Area(
                Polygon(
                    listOf(
                        Point(0, -400), Point(-300, 400), Point(300, 400)
                    )
                ).path
            )
        )

        // Draw 2 trees in the same place, one fainter, to give
        // some sense of solidity.
        drawTree(.3, area)
        drawTree(1.0, area)
    }

    private fun drawTree(opacity: Double, area: Area) {
        circles.clear()
        opacity(opacity)

        repeat(3000) {
            growCircle(opacity, area)
        }

    }

    val MAX_RADIUS = 50.0

    // Attempts to spawn a new tiny circle at a random location, and then expand it
    // until it either overlaps something or reaches maximum size.
    fun growCircle(opacity: Real, area: Area) {

        val c = seedCircle(area) ?: return

        while (c.radius < MAX_RADIUS) {
            c.radius += .5

            if (c.isUnplacable(area)) {
                c.radius *= .9
                break
            }
        }

        circles.add(c)
        c.draw(this, opacity)
    }

    // This to find a spot to place a new tiny circle.
    // If no such spot can be found, returns null.
    fun seedCircle(area: Area): Circle? {

        for (tries in 1..100) {
            val c = Circle(rand(canvas.size) - canvas.centre, 1.5)
            if (!c.isUnplacable(area)) return c
        }
        return null
    }
}
