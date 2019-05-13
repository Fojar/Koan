package drawings.examples

import koan.*
import kotlin.math.*

// Defines a drawing that is 600 by 400 pixels in size.
class Example1_lines : Drawing(600, 400) {

    // This function draws your drawing!
    override fun draw() {

        // The val keyword creates constants--named values that can't be changed.
        val a = Point(100, 100)
        val b = Point(500, 200)

        // Draw a line connecting two points.
        line(a, b)

        // You can change the thickness of lines that you draw.
        // Once you set this, it will affect all subsequently drawn lines.
        stroke(8)

        // You can just create a Point where you need it, without a name.
        line(b, Point(100, 300))

    }
}
