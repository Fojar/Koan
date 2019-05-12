package koan

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.streams.asSequence

class Canvas(val frame: JFrame) : JComponent() {

    private var currentDrawingClassName: String = ""

    private val defaultDrawing = DefaultDrawing()

    private var currentDrawing: Drawing = defaultDrawing
        set(drawing) {
            field = drawing
            val d = Dimension(drawing.width, drawing.height)
            size = d
            preferredSize = d
            drawing.resetInternal()
            repaint()

            SwingUtilities.invokeLater {
                drawing.drawInternal()
                repaint()
            }
        }

    private val parentClassLoader = javaClass.classLoader

    init {

        reloadDrawing()

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


    fun saveDrawing() {
        currentDrawing.save()
    }

    fun drawDrawing() {
        currentDrawing.drawInternal()
        repaint()
    }

    fun resetDrawing() {
        currentDrawing.resetInternal()
        drawDrawing()
    }

    fun reloadDrawing() {

        val drawing = getNewCurrentDrawing()

        val drawingName = currentDrawingClassName.replace('.', '/')

        if (drawing != null) {
            currentDrawing = drawing
            frame.title = drawingName
        } else {
            frame.title = "$drawingName [Invalid]"
            currentDrawing = defaultDrawing
        }
        frame.pack()
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
                currentDrawing.width * currentDrawing.sizeFactor,
                currentDrawing.height * currentDrawing.sizeFactor,
                null
            );
        }
    }

    fun loadDrawing(className: String) {
        currentDrawingClassName = className
        reloadDrawing()
    }

    fun getNewCurrentDrawing(): Drawing? {

        try {

            val drawingClass = DrawingClassLoader(parentClassLoader)
                .loadClass("drawings.$currentDrawingClassName")

            val thing = drawingClass.getConstructor().newInstance()

            if (thing is Drawing) {
                return thing
            }
        } catch (ex: Exception) {
            ex.cause?.printStackTrace()
        }

        return null
    }


}