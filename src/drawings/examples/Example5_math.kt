package drawings.examples

import koan.*
import kotlin.math.*

class Example5_math : Drawing(600, 400) {

    override fun reset() {

        // If you prefer your origin at the center of the canvas,
        // With y-coordinates increasing upward, you can have that!
        translate(halfWidth, halfHeight)
        scale(1, -1)

        clear()
    }

    override fun draw() {

        val altitude = rand(height) - halfHeight
        val amplitude = rand(10, 100)
        val frequency = rand(.005, .05)
        val phase = rand(TAU)

        opacity(.3)

        for (x in 0..width step 5) {

            val p = Point(
                x - halfWidth,
                altitude + sin(phase + x * frequency) * amplitude
            )

            fillCircle(p, 2.5)
        }

    }
}
