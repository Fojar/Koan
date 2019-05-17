package drawings.techniques

import koan.*
import kotlin.math.*

/**
 * Demonstrates drawing a wobbly line.
 */
class Random_walks : Drawing(600, 600) {

    // The drunkenness parameter controls how much deviation from
    // a straight line the path can take on each step.
    inner class Walker(val drunkenness: Double) {

        var position = canvas.centre
        var angle = rand(TAU)

        fun draw() {
            fillCircle(position, 1)

            angle += rand(-1, 1) * drunkenness
            val STEP_SIZE = 1.5
            val velocity = Point(cos(angle), sin(angle)) * STEP_SIZE
            position += velocity
        }
    }

    // The init block is only executed once, when the drawing is loaded.
    // Press backspace to reload the drawing.
    init {
        clear()
    }

    var walker: Walker = newWalker()

    // Try changing the drunkenness parameter!
    private fun newWalker() = Walker(.3)

    // Press enter to reset the walker to the centre, facing a random direction.
    override fun reset() {
        walker = newWalker()
    }

    // Press space to draw another 10 steps for the walker.
    override fun draw() {
        opacity(.5)

        repeat(10) {
            walker.draw()
        }
    }

}


