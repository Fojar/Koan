package drawings.techniques

import koan.*
import kotlin.math.*

/**
 * Demonstrates 2 ways of making circles blurry. One is deterministic
 * and the other is non-deterministic. Both employ the principle of layering
 * many translucent circles. The size of the blur is proportional to the size
 * of the circle.
 */
class Blurry_circles : Drawing(600, 400) {

    override fun draw() {

        color(hsv(.6, .7, .7))

        val size = rand(30, 130)

        glowingCircle(Point(150, 150), size)
        fuzzyCircle(Point(450, 250), size)
    }

    // Produces a uniform, consistent blur. Varies the radii of the
    // translucent circles, keeping the centre fixed.
    fun glowingCircle(centre: Point, radius: Double) {

        opacity(.05)
        for (percentage in 30 downTo 5) {
            fillCircle(centre, radius * (1 + percentage / 100.0))
        }

        opacity(1)
        fillCircle(centre, radius)
    }

    // Produces a non-uniform, varying blur. Varies the centre of
    // the translucent circles, keeping radius fixed.
    fun fuzzyCircle(centre: Point, radius: Double) {

        opacity(.05)
        repeat((radius).toInt()) {

            val θ = rand(TAU)       // An angle.
            val ρ = rand(.4)    // A scale factor.
            val offset = Point(cos(θ), sin(θ)) * radius * ρ
            fillCircle(centre + offset, radius)
        }

        opacity(1)
        fillCircle(centre, radius)
    }
}


