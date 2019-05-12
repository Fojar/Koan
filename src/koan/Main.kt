import koan.Canvas
import koan.Matte
import koan.gui.DrawingTreeView
import java.awt.*
import java.awt.event.ActionEvent
import javax.swing.*

fun main() {

    val canvas: Canvas

    val frame = JFrame().apply {

        canvas = Canvas(this)

        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
            background = Color(240, 240, 240)
            add(canvas, BorderLayout.CENTER)
            border = Matte()
        }

        contentPane.add(panel)
        pack()

        canvas.requestFocus()

        isResizable = false
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    val controlPanel = JFrame().apply {

        val buttonsPanel = JPanel().apply {
            layout = GridLayout(4, 1)

            addButton("<html><center><h2>Reload</h2>After code is changed</center></html>") {
                canvas.reloadDrawing()
            }
            addButton("<html><center><h2>Reset</h2>Calls reset and then draw</center></html>") {
                canvas.resetDrawing()
            }
            addButton("<html><center><h2>Draw</h2>Only call draws function</center></html>") {
                canvas.drawDrawing()
            }
            addButton("<html><center><h2>Save Image</h2>Into gallery folder</center></html>") {
                canvas.saveDrawing()
            }
            preferredSize = Dimension(200, 0)
        }
        contentPane.add(buttonsPanel, BorderLayout.EAST)


        val drawingBrowser = JPanel().apply {

            val drawingTree = DrawingTreeView {
                canvas.loadDrawing(it)
            }

            addButton("Refresh directories") {
                drawingTree.refreshModel()
            }
            add(JLabel("Select drawing:"))

            add(drawingTree)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            preferredSize = Dimension(250, 400)
        }
        contentPane.add(drawingBrowser, BorderLayout.CENTER)

        val consolePanel = JPanel().apply {

            add(JTextArea().apply {
                text = "Welcome to Koan."
            })

            preferredSize = Dimension(0, 150)
        }
        contentPane.add(consolePanel, BorderLayout.PAGE_END)


        pack()
        title = "Koan Control Panel"
        isResizable = false
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

    with(frame) {
        location = Point(controlPanel.width, 0)
        isVisible = true
    }

}

fun JPanel.addButton(title: String, clickHandler: (ActionEvent) -> Unit) {
    add(JButton(title).apply {
        addActionListener(clickHandler)
    })
}
