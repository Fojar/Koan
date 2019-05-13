package drawings.art

import koan.*
import kotlin.math.*

class Smoke_animated : Drawing(300, 800) {

    // This class stores a phase and can vary it at a constant rate.
    // The rate is chosen randomly from the given range.
    class AnimatedPhase(minDelta: Double, maxDelta: Double) {
        var phase = rand(TAU)
            private set

        val Δphase = rand(minDelta, maxDelta)

        fun step() {
            phase -= Δphase
        }
    }

    override fun draw() {

        val COLUMNS = 1
        for (x in 0 until COLUMNS) {
            drawSmoke(canvas.width * (x + 1.0) / (COLUMNS + 1), canvas.height * .9)
        }
    }

    val phases = listOf(
        // For the x-coordiante, small motion
        AnimatedPhase(.05, .1),
        AnimatedPhase(.05, .1),
        // For the y-coordinate, larger motion
        AnimatedPhase(.1, .2),
        AnimatedPhase(.1, .2)
    )

    val ceiling = rand(600, 700)
    val maxWidth = rand(100, 150)

    val hue = rand()

    val xPerturb = sum(
        PhasingSineWave(frequency(rand(2, 8)), rand(4, 16)) { phases[0].phase },
        PhasingSineWave(frequency(rand(8, 16)), rand(2, 8)) { phases[1].phase }
    )

    val yPerturb = sum(
        PhasingSineWave(frequency(rand(2, 8)), rand(4, 16)) { phases[2].phase },
        PhasingSineWave(frequency(rand(8, 16)), rand(2, 8)) { phases[3].phase }
    )

    private fun drawSmoke(bx: Double, by: Double) {

        clear()
        phases.forEach { it.step() }

        fun smokeRadius(q: Double) = 1 + q.pow(2.5) * maxWidth

        for (h in 0..ceiling step 1) {
            val q = h / ceiling

            val perturbationStrength = q.pow(.5)
            val x = xPerturb(q) * perturbationStrength
            val y = yPerturb(q) * perturbationStrength

            color(hsv(hue, (1 - q) * .5, .2 + q * .4))
            opacity((1 - q).pow(2.5) * .7)

            drawCircle(Point(bx + x, by - h + y), smokeRadius(q))
        }

    }

}
