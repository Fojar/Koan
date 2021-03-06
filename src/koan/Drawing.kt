package koan


import java.awt.*
import java.awt.datatransfer.*
import java.awt.datatransfer.DataFlavor.imageFlavor
import java.awt.event.KeyEvent
import java.awt.geom.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.*


data class CanvasSpec(val width: Int, val height: Int) {

	val halfWidth: Real = width / 2.0
	val halfHeight: Real = height / 2.0

	val size = Point(width, height)
	val centre = Point(halfWidth, halfHeight)
}

abstract class Drawing(val width: Int, val height: Int) {

	companion object {

		var resolutionMultiplier = 1
			set(value) {
				field = value.coerceIn(1..5)
			}

		private val line = Line2D.Double()
		private val ellipse = Ellipse2D.Double()
		private val rectangle = Rectangle2D.Double()
	}

	val canvas: CanvasSpec = CanvasSpec(width, height)

	val image: BufferedImage
	val graphics: Graphics2D
	private val originalTransform: AffineTransform
	private var customTransform: AffineTransform

	init {

		image = BufferedImage(
			width * resolutionMultiplier, height * resolutionMultiplier,
			BufferedImage.TYPE_INT_ARGB
		)

		graphics = (image.graphics as Graphics2D).apply {
			scale(resolutionMultiplier.toDouble(), resolutionMultiplier.toDouble())
			setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
			setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

			background = Color.WHITE
			color = Color.BLACK
		}

		originalTransform = graphics.transform
		customTransform = graphics.transform
	}

	open fun keyPressed(e: KeyEvent) = false

	fun translate(tx: Double, ty: Double) = graphics.translate(tx, ty)
	fun translate(tx: Int, ty: Int) = graphics.translate(tx, ty)
	fun translate(point: Point) = translate(point.x, point.y)

	fun scale(s: Double) = graphics.scale(s, s)
	fun scale(s: Int) = scale(s.toDouble())

	fun scale(sx: Double, sy: Double) = graphics.scale(sx, sy)
	fun scale(sx: Int, sy: Double) = graphics.scale(sx.toDouble(), sy)
	fun scale(sx: Double, sy: Int) = graphics.scale(sx, sy.toDouble())
	fun scale(sx: Int, sy: Int) = graphics.scale(sx.toDouble(), sy.toDouble())

	fun rotate(theta: Double) = graphics.rotate(theta)
	fun rotate(theta: Int) = graphics.rotate(theta.toDouble())


	fun line(a: Point, b: Point) {
		with(line) {
			x1 = a.x
			y1 = a.y
			x2 = b.x
			y2 = b.y
		}

		graphics.draw(line)
	}

	fun polyLine(points: List<Point>) {
		val path = pointsToPath(points)
		graphics.draw(path)
	}

	fun drawPolygon(points: List<Point>) {
		val path = pointsToPath(points).apply { closePath() }
		graphics.draw(path)
	}

	fun fillPolygon(points: List<Point>) {
		val path = pointsToPath(points).apply { closePath() }
		graphics.fill(path)
	}

	private fun pointsToPath(points: List<Point>): GeneralPath {
		val path = GeneralPath()
		path.moveTo(points[0].x, points[0].y)
		for (p in points.asSequence().drop(1)) path.lineTo(p.x, p.y)
		return path
	}


//region circle

	fun drawCircle(centre: Point, radius: Double) = graphics.draw(calcEllipse(centre, radius))
	fun drawCircle(centre: Point, radius: Int) = drawCircle(centre, radius.toDouble())

	fun fillCircle(centre: Point, radius: Double) = graphics.fill(calcEllipse(centre, radius))
	fun fillCircle(centre: Point, radius: Int) = fillCircle(centre, radius.toDouble())

	private fun calcEllipse(centre: Point, radius: Double) = ellipse.apply {
		x = centre.x - radius
		y = centre.y - radius
		width = radius * 2
		height = radius * 2
	}

//endregion


// region rectangle

	fun drawRectangle(a: Point, b: Point) = graphics.draw(calcRect(a, b))
	fun fillRectangle(a: Point, b: Point) = graphics.fill(calcRect(a, b))

	private fun calcRect(a: Point, b: Point) = rectangle.apply {
		this.x = min(a.x, b.x)
		this.y = min(a.y, b.y)

		this.width = abs(a.x - b.x)
		this.height = abs(a.y - b.y)
	}

// endregion


	fun clear() {
		val t = graphics.transform
		graphics.transform = originalTransform
		graphics.clearRect(0, 0, canvas.width, canvas.height)
		graphics.transform = t
	}

	fun clear(color: Color) {
		graphics.background = color
		clear()
	}

	fun resetInternal() {

		resetTransform()
		stroke(1)
		opacity(1)
		color(BLACK)
		reseedRNG()
		reset()
		customTransform = graphics.transform
	}

	fun resetTransform() {
		graphics.transform = AffineTransform(originalTransform)
	}

	open fun reset() {
		clear()
	}

	fun drawInternal() {
		graphics.transform = customTransform
		draw()
	}

	abstract fun draw()


	fun stroke(width: Double) =
		graphics.setStroke(BasicStroke(width.toFloat(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	fun stroke(width: Int) =
		graphics.setStroke(BasicStroke(width.toFloat(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	fun opacity(value: Double) = setAlpha((value.coerceIn(0.0, 1.0) * 255).toInt());
	fun opacity(value: Int) = setAlpha(value.coerceIn(0, 1) * 255);

	fun blendingMode(blendingMode: BlendingMode) {
		graphics.composite = blendingMode.composite
	}

	fun setAlpha(alpha: Int) {
		val c = graphics.color
		graphics.color = Color(c.red, c.green, c.blue, alpha)
	}

	fun color(c: Color) = setRGB(c.red, c.green, c.blue)

	private fun setRGB(red: Int, green: Int, blue: Int) {
		val c = graphics.color
		graphics.color = Color(red, green, blue, c.alpha)
	}

	fun copyToClipboard() {
		val imgSel = ImageSelection(image)
		Toolkit.getDefaultToolkit().systemClipboard.setContents(imgSel, null)
		println("Drawing copied to clipboard.")
	}

	fun save() {
		val filename = "${javaClass.simpleName}-$randomSeed.png"
		ImageIO.write(image, "png", File("gallery/$filename-$randomSeed.png"))
		println("Drawing saved to $filename.")
	}


	private val transformStack: Deque<AffineTransform> = ArrayDeque()

	fun pushTransform() = transformStack.push(graphics.transform)

	fun popTransform() {
		if (transformStack.isNotEmpty()) graphics.transform = transformStack.pop()
	}

	fun drawShape(shape: Shape) = graphics.draw(shape.path)
	fun fillShape(shape: Shape) = graphics.fill(shape.path)

}

class ImageSelection(private val image: Image) : Transferable {

	private val flavors = arrayOf(imageFlavor)

	override fun getTransferDataFlavors() = flavors

	override fun isDataFlavorSupported(flavor: DataFlavor) = flavor == imageFlavor

	override fun getTransferData(flavor: DataFlavor) =
		if (flavor == imageFlavor) image
		else throw UnsupportedFlavorException(flavor)
}


