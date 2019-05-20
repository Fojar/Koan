package drawings.examples

import koan.*
import kotlin.math.*

// Demonstrates the use of different blending modes.
class Example6_blending : Drawing(600, 200) {

    override fun reset() {
        clear(GRAY)
    }

    override fun draw() {

        // Blending modes work with translucency. Try it!
        opacity(1)

        // The additive mode is like light, that gets brighter
        // and tends to white as you add different colours.
        blendingMode(BlendingMode.ADDITIVE)
        triCircle(Point(100, canvas.halfHeight))

        // The subtractive mode removes colour, tending toward
        // black. Note that the result on a white background
        // would be the opposite of what you specify!
        blendingMode(BlendingMode.SUBTRACTIVE)
        triCircle(Point(300, canvas.halfHeight))

        // The normal mode is what you're used to. It tends toward
        // whatever colour you specify.
        blendingMode(BlendingMode.NORMAL)
        triCircle(Point(500, canvas.halfHeight))
    }


    // Draws 3 overlapping circles of the 3 primary colours.
    fun triCircle(centre: Point) {

        val radius = 50.0
        val separation = 30.0

        pushTransform()
        translate(centre)

        color(RED)
        fillCircle(angleToPoint(0 * TAU / 3) * separation, radius)
        color(GREEN)
        fillCircle(angleToPoint(1 * TAU / 3) * separation, radius)
        color(BLUE)
        fillCircle(angleToPoint(2 * TAU / 3) * separation, radius)

        popTransform()
    }
}
