package drawings.techniques

import koan.*
import kotlin.math.*

class Wavelets : Drawing(600, 600) {

    override fun draw() {

        for (i in 0..1) {
            opacity(.2 + i * .4)
            color(hsv(rand(), .5, .5))
            drawWaves(2 - i)
        }

        // Add a fuzzy white fringe around the edges of the canvas.
        color(WHITE)
        opacity(.1)

        for (step in 5 downTo 1) {
            val radius = step * 10
            repeat(1000) {
                drawCircle(Point(0, rand(canvas.height)), rand(radius))
                drawCircle(Point(canvas.width, rand(canvas.height)), rand(radius))
                drawCircle(Point(rand(canvas.width), 0), rand(radius))
                drawCircle(Point(rand(canvas.width), canvas.height), rand(radius))
            }
        }

    }

    fun drawWaves(size: Int) {

        // The frequency function returns a constant k such that sin(k*x) will
        // produce the given number of cycles as x ranges from 0 to 1.
        // In this case we want somewhere between 1 and 3 cycles across the canvas.
        val k = frequency(rand(1, 4))

        val phase = rand(TAU)

        // The amount of extra phase per wave drawn.
        val shiftPerWave = rand(-.03, .03)

        // The amount of phase by which the bottom of the wave is offset from the top.
        val bias = rand(-1, 1) * .5

        // The vertical distance between each wave.
        val spacing = 30 * size

        for (y in -rint(spacing)..canvas.height + spacing step spacing) {

            stroke(rand(.5, 1.5))

            // A Gaussian function has a peak of 1 at the first parameter (centre) and fades to 0
            // as distance from centre approaches infinity. The rate of decay is controlled by the
            // second parameter (deviation); larger values yield a gentler decay.
            val g = Gaussian(rand(), .1 * size)

            for (x in 0..canvas.width step 1) {

                val q = 1.0 * x / canvas.width

                val amplitude = 5 + 5* g(q)

                val shift = y * shiftPerWave

                // The thickness of the wavelet at position q.
                val height = g(q) * 10 * size

                if (height < .2) continue

                val top = sin(phase + shift + q * k) * amplitude - height - rand()
                val bottom = sin(phase + shift + bias + q * k) * amplitude + height + rand()

                val xTop = x + rand(2)
                val xBottom = x + rand(2)

                line(Point(xTop, y - top), Point(xBottom, y - bottom))
            }
        }
    }


}
