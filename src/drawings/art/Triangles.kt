package drawings.art

import koan.*

class Triangles : Drawing(480, 720) {

    // The length of the side of the squares in the drawing.
    // Each square will contain a colored triangle.
    val SIZE = 40
    val HALF_SIZE = SIZE / 2

    // Create a right-angled triangle, with the
    // origin at the center of the hypotenuse.
    val triangle = Polygon(
        listOf(
            Point(-HALF_SIZE, -HALF_SIZE),
            Point(HALF_SIZE, -HALF_SIZE),
            Point(-HALF_SIZE, HALF_SIZE)
        )
    )

    // These colors were sampled from the photo.
    val colors = listOf(
        rgb(0.792, 0.345, 0.275),   // Red
        rgb(0.863, 0.592, 0.286),   // Orange
        rgb(0.906, 0.792, 0.157),   // Yellow
        rgb(0.388, 0.431, 0.196),   // Green
        rgb(0.365, 0.369, 0.484),   // Blue
        rgb(0.502, 0.263, 0.259)    // Purple
    )

    override fun draw() {

        // Clear the background to a dark gray.
        clear(rgb(0.14, 0.14, 0.14))

        for (x in 0 until 12) {
            for (y in 0 until 18) {

                // This saves the current transformation.
                pushTransform()

                // Move the origin to where next triangle should be drawn.
                val cx = x * SIZE + HALF_SIZE
                val cy = y * SIZE + HALF_SIZE
                translate(cx, cy)

                // Pick one of the colors randomly.
                color(colors[rint(colors.size)])

                // Rotate some random multiple of 90 degrees.
                rotate(TAU * rint(4) / 4)

                fillShape(triangle)

                // Restore the previously saved transformation.
                popTransform()
            }
        }

    }

}