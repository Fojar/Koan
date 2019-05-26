package drawings.techniques

import koan.*
import kotlin.math.*

class DeJong : Drawing(500, 500) {

    var a: Real = 0.0
    var b: Real = 0.0
    var c: Real = 0.0
    var d: Real = 0.0

    val zoom = min(canvas.width, canvas.height) / 5.0

    override fun reset() {
        clear()
        translate(canvas.centre)
        opacity(.05)

        a = rand(-PI, PI)
        b = rand(-PI, PI)
        c = rand(-PI, PI)
        d = rand(-PI, PI)
    }

    override fun draw() {

        var p = Point(rand(-2, 2), rand(-2, 2))
        repeat(10000) {
            p = deJong(p)
            fillCircle(p * zoom, 1)
        }
    }

    private fun deJong(p: Point): Point {
        val tx = sin(a * p.y) - cos(b * p.x)
        val ty = sin(c * p.x) - cos(d * p.y)
        return Point(tx, ty)
    }

}
