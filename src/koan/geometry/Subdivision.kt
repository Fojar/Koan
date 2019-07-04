package koan

import kotlin.math.*

fun List<Point>.closedCrinkled(crinkliness: Real): List<Point> {
    return crinkle(this, this.size, crinkliness)
}

fun List<Point>.openCrinkled(crinkliness: Real): List<Point> {
    return crinkle(this, this.size - 1, crinkliness).apply {
        add(this.last())
    }
}

private fun crinkle(points: List<Point>, n: Int, crinkliness: Real): MutableList<Point> {

    val result = mutableListOf<Point>()

    for (i in 0 until n) {

        val a = points[i]
        val b = points[(i + 1) % points.size]

        val newPoint = midpointPerturbed(a, b, crinkliness) ?: continue

        result.add(a)
        result.add(newPoint)
    }

    return result
}

private fun midpointPerturbed(a: Point, b: Point, delta: Real): Point? {
    val ab = b - a
    if (ab.magnitude <= 1) return null

    val midPoint = (a + b) / 2
    val normal = ab.perpendicular

    val perturbation = normal * rand(-1, 1) * delta
    return midPoint + perturbation
}


fun List<Point>.openSmoothed(): List<Point> {

    var result = this as MutableList<Point>

    do {
        val newResult = chaikin(result, 1, result.size - 1)
        newResult.add(0, first())
        newResult.add(last())

        val grown = newResult.size > result.size
        result = newResult

    } while (grown)

    return result
}

fun List<Point>.closedSmoothed(): List<Point> {

    var result = this

    do {
        val newResult = chaikin(result, 0, result.size)
        val grown = newResult.size > result.size
        result = newResult
    } while (grown)

    return result
}

private fun chaikin(source: List<Point>, start: Int, finish: Int): MutableList<Point> {

    val result = mutableListOf<Point>()

    for (i in start until finish) {

        val a = source[(i + source.size - 1) % source.size]
        val b = source[i]
        val c = source[(i + 1) % source.size]

        val ab = b - a
        val bc = c - b

        val abLength = ab.magnitude
        val bcLength = bc.magnitude

        val dot = (ab dot bc) / (abLength * bcLength)

        var chop = false
        if (dot < .8) chop = true
        else {
            val approxTheta = acos(dot) //(1 - dot) / 2
            val deviation = approxTheta * max(abLength, bcLength)
            if (deviation > 1) chop = true
        }

        if (chop) {
            val left = ab * .75 + a
            val right = bc * .25 + b

            result.add(left)
            result.add(right)

        } else {
            result.add(b)
        }
    }

    return result
}


