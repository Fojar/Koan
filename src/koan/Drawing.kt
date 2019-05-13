package koan

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.geom.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.*
import java.util.Deque

data class CanvasSpec(val width: Int, val height: Int) {

    val halfWidth: Double = width / 2.0
    val halfHeight: Double = height / 2.0

    val size = Point(width, height)
    val centre = Point(halfWidth, halfHeight)
}

abstract class Drawing(width: Int, height: Int) {

    val canvas: CanvasSpec = CanvasSpec(width, height)

    val sizeFactor: Int = 1

    val image: BufferedImage

    private var customTransform: AffineTransform
    private val originalTransform: AffineTransform

    val graphics: Graphics2D

    private val line = Line2D.Double()
    private val ellipse = Ellipse2D.Double()
    private val rectangle = Rectangle2D.Double()

    init {

        image = BufferedImage(width * sizeFactor, height * sizeFactor, BufferedImage.TYPE_INT_ARGB)
        graphics = (image.graphics as Graphics2D).apply {
            scale(sizeFactor.toDouble(), sizeFactor.toDouble())
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

            background = Color.WHITE
            color = Color.BLACK
        }

        originalTransform = graphics.transform
        customTransform = graphics.transform

    }

    open fun keyPressed(e: KeyEvent) = false

    protected fun translate(tx: Double, ty: Double) = graphics.translate(tx, ty)
    protected fun translate(tx: Int, ty: Int) = graphics.translate(tx, ty)

    protected fun scale(s: Double) = graphics.scale(s, s)
    protected fun scale(s: Int) = scale(s.toDouble())

    fun scale(sx: Double, sy: Double) = graphics.scale(sx, sy)
    fun scale(sx: Int, sy: Double) = graphics.scale(sx.toDouble(), sy)
    fun scale(sx: Double, sy: Int) = graphics.scale(sx, sy.toDouble())
    fun scale(sx: Int, sy: Int) = graphics.scale(sx.toDouble(), sy.toDouble())


    protected fun rotate(theta: Double) = graphics.rotate(theta)
    protected fun rotate(theta: Int) = graphics.rotate(theta.toDouble())


    protected fun line(a: Point, b: Point) {
        with(line) {
            x1 = a.x
            y1 = a.y
            x2 = b.x
            y2 = b.y
        }

        graphics.draw(line)
    }


    //region circle

    protected fun drawCircle(centre: Point, radius: Double) = graphics.draw(calcEllipse(centre, radius))
    protected fun drawCircle(centre: Point, radius: Int) = drawCircle(centre, radius.toDouble())

    protected fun fillCircle(centre: Point, radius: Double) = graphics.fill(calcEllipse(centre, radius))
    protected fun fillCircle(centre: Point, radius: Int) = fillCircle(centre, radius.toDouble())

    private fun calcEllipse(centre: Point, radius: Double) = ellipse.apply {
        x = centre.x - radius
        y = centre.y - radius
        width = radius * 2
        height = radius * 2
    }

    //endregion


    // region rectangle

    protected fun drawRectangle(a: Point, b: Point) = graphics.draw(calcRect(a, b))
    protected fun fillRectangle(a: Point, b: Point) = graphics.fill(calcRect(a, b))

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

    fun clear(color: Color)  {
        graphics.background = color
        clear()
    }

    fun resetInternal() {
        graphics.transform = originalTransform
        stroke(1)
        opacity(1)
        color(BLACK)
        reseedRNG()
        reset()
        customTransform = graphics.transform
    }

    open fun reset() {
        clear()
    }

    fun drawInternal() {
        graphics.transform = customTransform
        draw()
    }

    abstract fun draw()


    protected fun stroke(width: Double) = graphics.setStroke(BasicStroke(width.toFloat()));
    protected fun stroke(width: Int) = graphics.setStroke(BasicStroke(width.toFloat()));

    protected fun opacity(value: Double) = setAlpha((value.coerceIn(0.0, 1.0) * 255).toInt());
    protected fun opacity(value: Int) = setAlpha(value.coerceIn(0, 1) * 255);

    private fun setAlpha(alpha: Int) {
        val c = graphics.color
        graphics.color = Color(c.red, c.green, c.blue, alpha)
    }

    protected fun color(c: Color) = setRGB(c.red, c.green, c.blue)

    private fun setRGB(red: Int, green: Int, blue: Int) {
        val c = graphics.color
        graphics.color = Color(red, green, blue, c.alpha)
    }


    fun save() {
        val filename = this.javaClass.simpleName
        ImageIO.write(image, "png", File("gallery/$filename-$randomSeed.png"))
    }


    private val transformStack: Deque<AffineTransform> = ArrayDeque()

    protected fun pushTransform() = transformStack.push(graphics.transform)

    protected fun popTransform() {
        if (transformStack.isNotEmpty()) graphics.transform = transformStack.pop()
    }

    fun drawShape(shape: Shape) = graphics.draw(shape.path)
    fun fillShape(shape: Shape) = graphics.fill(shape.path)


}


