package drawings

import koan.*
import kotlin.math.*

class Example3_colors : Drawing(600, 400) {

    override fun draw() {

        stroke(.5)

        // The for statement does loops.
        // In this case, the variable x will range from 0 to 400, increasing by 50 each time.
        for (x in 0..400 step 50) {

            // Dividing x by 400.0 gives us a number ranging from 0 to 1.
            // Note that you have to use 400.0 instead of just 400 to get a real number!
            // Writing x / 400 would only do integer division, always giving 0 (ignoring remainder).
            val q = x / 400.0


            // The hsv function returns a color using hue, saturation and value.
            // All those parameters range from 0 to 1.
            color(hsv(q, 1, 1))

            fillCircle(Point(100 + x, 100), 20)

            color(BLACK)

            // You can change the opacity of subsequent drawing commands thus:
            opacity(q)

            fillCircle(Point(100 + x, 250), 40)

            opacity(1)
            drawCircle(Point(100 + x, 250), 40)

        }


    }
}
