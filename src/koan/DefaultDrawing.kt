package koan

import java.awt.Color

class DefaultDrawing : Drawing(300, 300) {

    override fun draw() {

        color(Color.RED)
        stroke(10)

        line(Point(0, 0), Point(canvas.width, canvas.height))
        line(Point(canvas.width, 0), Point(0, canvas.height))
    }

}


