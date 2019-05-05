package drawings

import koan.*
import kotlin.math.*

class Example8_smokes : Drawing(1200, 800) {

    override fun draw() {

        val COLUMNS = 7
        for (x in 0 until COLUMNS) {
            drawSmoke(width * (x + 1.0) / (COLUMNS + 1), height * .9)
        }
    }

    private fun drawSmoke(bx: Double, by: Double) {

        val height = rand(600, 700)
        val maxWidth = rand(100, 150)

        val hue = rand()

        val xPerturb = sum(
            waveFunction(frequency(rand(2, 8)), rand(4, 16), rand(TAU)),
            waveFunction(frequency(rand(8, 16)), rand(2, 8), rand(TAU))
        )

        val yPerturb = sum(
            waveFunction(frequency(rand(2, 8)), rand(4, 16), rand(TAU)),
            waveFunction(frequency(rand(8, 16)), rand(2, 8), rand(TAU))
        )

        fun smokeRadius(q: Double) = 1 + q.pow(2.5) * maxWidth

        for (h in 0..height step 1) {
            val q = h / height

            val perturbationStrength = q.pow(.5)
            val x = xPerturb(q) * perturbationStrength
            val y = yPerturb(q) * perturbationStrength

            color(hsv(hue, (1 - q) * .5, .2 + q * .4))
            opacity((1 - q).pow(2.5) * .7)

            drawCircle(Point(bx + x, by - h + y), smokeRadius(q))
        }

    }

}
