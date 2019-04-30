package drawings

import koan.*
import kotlin.math.*

class Example2_circles : Drawing(600, 400) {

    override fun draw() {

        // The var keyword creates a variable that can be changed.
        var c = Point(width / 3, height / 2)

        // The second parameter for the drawCircle function is the radius.
        drawCircle(c, 50)

        // You can change the value of a variable like so:
        c = c + Point(100, 0)

        // Here's a shortcut that does the same thing:
        c += Point(100, 0)

        // So now the x-coordinate of c is 200 pixels to the right of where it was before.
        fillCircle(c, 50)

    }
}
