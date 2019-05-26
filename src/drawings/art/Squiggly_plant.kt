package drawings.art

import koan.*
import kotlin.math.*

class Squiggly_plant : Drawing(600, 600) {

    inner class Strand(val hue: Real) {

        var position = Point(canvas.halfWidth + rand(-1, 1) * 5, canvas.height * .9)

        var angle = -TAU / 4    // Pointing upward.
        var turn = 0.0
        var deltaTurn = 0.0

        var drunkenness = 0.0
        var deltaDrunk = 0.0

        fun draw() {

            color(hsv(hue + deltaDrunk * 200000, .7, .5))
            fillCircle(position, 1)

            deltaDrunk += .000000001
            drunkenness += deltaDrunk

            deltaTurn += rand(-1, 1) * drunkenness
            turn += deltaTurn
            turn *= 1.008
            angle += turn

            position += angleToPoint(angle)
        }
    }

    var hue: Real = 0.0

    override fun reset() {
        clear()
        opacity(.2)
        hue = rand(-1, 1)
    }

    override fun draw() {

        repeat(30) {
            val walker = Strand(hue)
            repeat(1000) {
                walker.draw()
            }
        }

    }

}


