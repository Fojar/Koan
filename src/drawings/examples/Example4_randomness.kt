package drawings.examples

import koan.*
import kotlin.math.*

class Example4_randomness : Drawing(600, 400) {

    // This function is called when you press backspace.
    // It can be used to reset a drawing's state however you like.
    override fun reset() {

        // You can specify a random seed to always get the same sequence.
        // If you don't set a seed when you reset, you get a different sequence each time.
        // Note that there has to be a U after the number, because it's unsigned!
        randomSeed = 123456789U

        // If you use the reset function, your canvas won't automatically be cleared.
        clear()
    }

    override fun draw() {

        // An easy way to repeat some actions:
        repeat(5) {

            // Make a random point somewhere on the canvas.
            val p = Point(rand(width), rand(height))

            // Here's how to get a random number in a given range:
            val radius = rand(3, 13)

            drawCircle(p, radius)
        }

    }
}
