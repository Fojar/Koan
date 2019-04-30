package koan

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JComponent
import javax.swing.JFrame
import java.nio.file.*
import java.util.ArrayList
import javax.swing.SwingUtilities
import kotlin.streams.asSequence

class Canvas(val frame: JFrame) : JComponent() {

    val drawingsClasses = getListOfClassesInDrawingsPackage()
    var currentDrawingIndex = 0

    val defaultDrawing = DefaultDrawing()

    var drawing: Drawing = defaultDrawing
        set(drawing) {
            field = drawing
            val d = Dimension(drawing.width, drawing.height)
            size = d
            preferredSize = d
            drawing.clear()
            repaint()

            SwingUtilities.invokeLater {
                drawing.drawInternal()
                repaint()
            }
        }

    val parentClassLoader = javaClass.classLoader

    init {

        reloadDrawing()

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {

                when (e.keyCode) {
                    KeyEvent.VK_SPACE -> {
                        drawing.drawInternal()
                        repaint()
                    }
                    KeyEvent.VK_BACK_SPACE -> {
                        drawing.resetInternal()
                        drawing.drawInternal()
                        repaint()
                    }
                    KeyEvent.VK_INSERT -> drawing.save()
                    KeyEvent.VK_ENTER -> reloadDrawing()
                    KeyEvent.VK_PAGE_UP -> previousDrawing()
                    KeyEvent.VK_PAGE_DOWN -> nextDrawing()
                    else -> {
                        if (drawing.keyPressed(e)) {
                            drawing.drawInternal()
                            repaint()
                        }
                    }
                }
            }
        })

    }


    fun nextDrawing() {
        currentDrawingIndex = (currentDrawingIndex + 1) % drawingsClasses.size
        reloadDrawing()
    }

    fun previousDrawing() {
        currentDrawingIndex = (currentDrawingIndex + drawingsClasses.size - 1) % drawingsClasses.size
        reloadDrawing()
    }


    fun getListOfClassesInDrawingsPackage(): MutableList<String> {
        val url = javaClass.getResource("")
        val stuffPath = Paths.get(url.toURI())
        val drawingsPath = stuffPath.parent.resolve("drawings")

        val fileNames = ArrayList<String>()

        val classMatcher = FileSystems.getDefault().getPathMatcher("glob:**.class");

        return Files.list(drawingsPath).asSequence()
            .filter { !it.toString().contains('$') && classMatcher.matches(it) }
            .map { it.fileName.toString().substringBeforeLast('.') }
            .sorted()
            .toMutableList()
    }


    fun getNewCurrentDrawing(): Drawing {

        if (drawingsClasses.isEmpty()) return defaultDrawing

        try {

            while (!drawingsClasses.isEmpty()) {
                val className = drawingsClasses[currentDrawingIndex]
                frame.title = className

                val drawingClass = DrawingClassLoader(parentClassLoader).loadClass("drawings.$className")
                val thing = drawingClass.getConstructor().newInstance()

                if (thing is Drawing) {
                    thing.resetInternal()
                    return thing
                } else {
                    println("Removing non-Drawing class: $className.")
                    drawingsClasses.removeAt(currentDrawingIndex)
                    if (currentDrawingIndex == drawingsClasses.size) currentDrawingIndex = 0
                }
            }

        } catch (ex: Exception) {
            ex.cause?.printStackTrace()
        }

        return defaultDrawing
    }

    fun reloadDrawing() {

        drawing = getNewCurrentDrawing()
        frame.pack()
    }

    override fun paintComponent(g: Graphics) {

        (g as Graphics2D).apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            drawImage(
                drawing.image,
                0, 0, width, height,
                0, 0, drawing.width * drawing.sizeFactor, drawing.height * drawing.sizeFactor,
                null
            );
        }
    }

}