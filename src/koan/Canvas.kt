package koan

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.roundToInt

class Canvas(private val frame: JFrame) : JComponent() {

	private val defaultDrawing = DefaultDrawing()

	var scaleFactor = 1.0
		set(value) {
			field = value
			updateScale()
		}

	private var currentDrawingClassName: String = ""

	private var currentDrawing: Drawing = defaultDrawing
		set(drawing) {
			field = drawing

			drawing.resetInternal()
			updateScale()

			SwingUtilities.invokeLater {
				drawing.drawInternal()
				repaint()
			}
		}

	private val parentClassLoader = javaClass.classLoader

	init {

		addKeyListener(object : KeyAdapter() {
			override fun keyPressed(e: KeyEvent) {

				when (e.keyCode) {
					KeyEvent.VK_BACK_SPACE -> reloadDrawing()
					KeyEvent.VK_ENTER -> resetDrawing()
					KeyEvent.VK_SPACE -> drawDrawing()
					else -> {
						if (currentDrawing.keyPressed(e)) {
							currentDrawing.drawInternal()
							repaint()
						}
					}
				}
			}
		})

	}


	private fun updateScale() {
		val windowWidth = (currentDrawing.canvas.width * scaleFactor).roundToInt()
		val windowHeight = (currentDrawing.canvas.height * scaleFactor).roundToInt()
		val newSize = Dimension(windowWidth, windowHeight)

		if (newSize != size) {
			size = newSize
			preferredSize = size
			frame.pack()
		}
		repaint()
	}

	fun reloadDrawing() {

		val drawing = getNewCurrentDrawing()
		val drawingName = currentDrawingClassName.replace('.', '/')

		if (drawing != null) {
			clearConsole()
			println("Drawing $drawingName loaded.")
			frame.title = "Koan drawing: $drawingName"
			currentDrawing = drawing
		} else {
			frame.title = "Koan drawing: $drawingName [Invalid]"
			currentDrawing = defaultDrawing
		}
		frame.pack()
	}

	fun resetDrawing() {
		currentDrawing.resetInternal()
		drawDrawing()
	}

	fun drawDrawing() {
		currentDrawing.drawInternal()
		repaint()
	}

	fun copyToClipboard() {
		currentDrawing.copyToClipboard()
	}

	fun saveDrawing() {
		currentDrawing.save()
	}


	override fun paintComponent(g: Graphics) {

		(g as Graphics2D).apply {
			setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			drawImage(
				currentDrawing.image,
				0,
				0,
				width,
				height,
				0,
				0,
				currentDrawing.image.width,
				currentDrawing.image.height,
				null
			);
		}
	}

	fun loadDrawing(className: String) {
		currentDrawingClassName = className
		reloadDrawing()
	}

	fun getNewCurrentDrawing() = try {

		val drawingClass = DrawingClassLoader(parentClassLoader)
			.loadClass("drawings.$currentDrawingClassName")

		drawingClass.getConstructor().newInstance() as? Drawing

	} catch (ex: Exception) {
		ex.cause?.printStackTrace()
		null
	}

}