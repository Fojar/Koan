package koan

import java.awt.geom.GeneralPath

class Shape(points: List<Point>) {

    val path = GeneralPath()

    init {
        path.moveTo(points[0].x, points[0].y)
        for (p in points.asSequence().drop(1)) path.lineTo(p.x, p.y)
        path.closePath()
   }

}
