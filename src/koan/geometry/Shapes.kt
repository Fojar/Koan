package koan

import java.awt.geom.GeneralPath


interface Shape {
    val path: GeneralPath
}

class Polygon(points: List<Point>): Shape {

    override val path = GeneralPath()

    init {
        path.moveTo(points[0].x, points[0].y)
        for (p in points.asSequence().drop(1)) path.lineTo(p.x, p.y)
        path.closePath()
   }

}
