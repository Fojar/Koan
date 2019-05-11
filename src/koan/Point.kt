package koan

import java.lang.Math.hypot

data class Point(val x: Double, val y: Double) {
    operator fun times(c: Int) = Point(c * x, c * y)
    operator fun times(c: Double) = Point(c * x, c * y)

    operator fun plus(b: Point) = Point(x + b.x, y + b.y)
    operator fun minus(b: Point) = Point(x - b.x, y - b.y)

    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())
    constructor(x: Double, y: Int) : this(x, y.toDouble())
    constructor(x: Int, y: Double) : this(x.toDouble(), y)

    infix fun distanceTo(other: Point) = hypot(x - other.x, y - other.y)

    companion object {
        val ZERO = Point(0, 0)
    }
}


