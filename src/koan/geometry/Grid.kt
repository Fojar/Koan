package koan

fun gridOver(
    size: Point, columns: Int, rows: Int,
    function: (Point) -> Unit
) {

    for (y in 1..rows) {
        for (x in 1..columns) {

            val p = Point(
                size.x * x / (columns + 1),
                size.y * y / (rows + 1)
            )

            function(p)
        }
    }
}
