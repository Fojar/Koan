package drawings.techniques

import koan.*

class Smoothing : Drawing(500, 500) {

    override fun draw() {

        clear()

        val points = List(5) {
            rand(canvas.size)
        }

        color(ORANGE); stroke(5); opacity(.5)

        drawPolygon(points)
        points.forEach { drawCircle(it, 5) }

        color(BLUE); stroke(3); opacity(1)

        // The closedSmoothed function on a list of points produces a new list of
        // points that approximate a closed Chaikin curve for the original points.
        drawPolygon(points.closedSmoothed())

        // If you want an open curve instead, replace the above line with the following:
        //polyLine(points.openSmoothed())

    }

}
