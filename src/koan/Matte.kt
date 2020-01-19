package koan

import javax.swing.border.AbstractBorder
import java.awt.*
import kotlin.math.roundToInt


class Matte() : AbstractBorder() {

	private var gap = 60
	private var frameThickness = 10

	val highlight = Color(240, 240, 240)
	val shadow = Color(200, 200, 200)


	fun updateScale(scaleFactor: Real) {
		gap = (60 * scaleFactor).roundToInt()
		frameThickness = (10 * scaleFactor).roundToInt()
	}

	override fun getBorderInsets(c: Component): Insets {
		return getBorderInsets(c, Insets(gap, gap, gap, gap))
	}

	override fun getBorderInsets(c: Component, insets: Insets): Insets {
		insets.bottom = gap
		insets.right = gap
		insets.top = gap
		insets.left = gap
		return insets
	}

	override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {

		g.color = Color.DARK_GRAY

		g.fillRect(0, 0, width, frameThickness)
		g.fillRect(0, height - frameThickness, width, frameThickness)
		g.fillRect(0, frameThickness, frameThickness, height - frameThickness * 2)
		g.fillRect(width - frameThickness, frameThickness, frameThickness, height - frameThickness * 2)

		fun horizontalLine(x1: Int, x2: Int, y: Int) = g.drawLine(x1, y, x2, y)
		fun verticalLine(y1: Int, y2: Int, x: Int) = g.drawLine(x, y1, x, y2)

		g.color = shadow

		horizontalLine(gap, width - gap - 1, gap - 1)
		horizontalLine(gap - 1, width - gap, gap - 2)

		verticalLine(gap - 1, height - gap - 1, gap - 1)
		verticalLine(gap - 2, height - gap, gap - 2)

		g.color = highlight

		horizontalLine(gap, width - gap - 1, height - gap)
		horizontalLine(gap - 1, width - gap, height - gap + 1)

		verticalLine(gap, height - gap, width - gap)
		verticalLine(gap - 1, height - gap + 1, width - gap + 1)

	}

}