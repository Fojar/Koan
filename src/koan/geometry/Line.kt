package koan

import koan.Real


data class Line(val A: Real, val B: Real, val C: Real) {

    infix fun intersect(that: Line): Point? {

        val det = this.A * that.B - that.A * this.B
        if (det == 0.0) return null

        val x = (that.B * this.C - this.B * that.C) / det
        val y = (this.A * that.C - that.A * this.C) / det

        return Point(x, y)
    }

}


fun Line(first: Point, second: Point): Line {

    val A = second.y - first.y
    val B = first.x - second.x
    val C = A * first.x + B * first.y

    return Line(A, B, C)
}
