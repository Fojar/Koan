package drawings

import koan.*
import kotlin.math.*

class Example7_flowers : Drawing(1200, 400) {

    override fun reset() {
        opacity(.2)
        clear()
    }

    override fun draw() {

        val ROWS = 1
        val COLUMNS = 3

        for (y in 0 until ROWS) {
            for (x in 0 until COLUMNS) {

                pushTransform()
                translate(
                    width * (x + .5) / COLUMNS,
                    height * (y + .5) / ROWS
                )

                drawFlower()
                popTransform()
            }
        }
    }

    fun drawFlower() {

        val PETALS = rint(5..13)

        val CENTRAL_WIDTH = rand(20, 70)
        val LENGTH = rand(150, 190)

        val ROTATION = rand(TAU)

        val HUE_OFFSET = rand()
        val HUE_RANGE = rand(-.2, .2)

        val PHASE = rand(TAU)
        val FREQUENCY = rand(.1)
        val WOBBLE = rand(7)

        val PETAL_TAPER = rand(.5, 2)
        val PETAL_CURVE = rand(.2, .8)

        // Returns the width of a petal at the given fraction along its length.
        fun petalWidth(q: Double): Double {
            val curve = (sin(q * TAU * 3 / 4) + 1) / 2
            val taper = (1 - q).pow(PETAL_TAPER)
            return CENTRAL_WIDTH * (taper..curve).lerp(PETAL_CURVE)
        }

        for (x in 0..LENGTH step 2) {

            val q = x / LENGTH

            for (petal in 0 until PETALS) {

                pushTransform()
                rotate(ROTATION + TAU * petal / PETALS)

                color(hsv(HUE_OFFSET + HUE_RANGE * q, .8, .7))

                val y = sin(PHASE + x * FREQUENCY) * WOBBLE
                drawCircle(Point(x, y), petalWidth(q))
                popTransform()
            }

        }

    }

}