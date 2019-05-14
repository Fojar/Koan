package drawings.techniques

import koan.*
import kotlin.math.*

class Wave_weave : Drawing(500, 500) {

    override fun draw() {

        for (i in 0..1) {
            opacity(.2 + i * .4)
            color(hsv(rand(), .5, .8))
            drawWaves(20 - i * 10)
        }

        // Add a fuzzy white fringe around the edges of the canvas.
        color(WHITE)
        opacity(.1)

        for (step in 5 downTo 1) {
            val radius = step * 5
            repeat(500) {
                drawCircle(Point(0, rand(canvas.height)), rand(radius))
                drawCircle(Point(canvas.width, rand(canvas.height)), rand(radius))
                drawCircle(Point(rand(canvas.width), 0), rand(radius))
                drawCircle(Point(rand(canvas.width), canvas.height), rand(radius))
            }
        }

    }

    // Draws waves having the given height.
    fun drawWaves(height: Int) {

        // The frequency function returns a constant k such that sin(k*x) will
        // produce the given number of cycles as x ranges from 0 to 1.
        // In this case we want somewhere between 1 and 3 cycles across the canvas.
        val k = frequency(rand(1, 3))

        val phase = rand(TAU)

        // The amount of extra phase per wave drawn.
        val shiftPerWave = rand(-.03, .03)

        // The amount of phase by which the bottom of the wave is offset from the top.
        val bias = rand(-1, 1) * .5

        // The vertical distance between each wave.
        val spacing = 10 * height.pow(.5)

        for (y in -rand(spacing)..canvas.height + spacing step spacing) {

            // This affects the entire wave.
            stroke(rand(.5, 1.5))

            for (x in 0..canvas.width) {

                val q = 1.0 * x / canvas.width

                val amplitude = 10

                val shift = y * shiftPerWave
                val top = sin(phase + shift + q * k) * amplitude
                val bottom = sin(phase + shift + bias + q * k) * amplitude

                val xTop = x + rand(2)
                val xBottom = x + rand(2)
                line(Point(xTop, y - top), Point(xBottom, y + height - bottom))

            }
        }
    }


}
