import koan.Canvas
import koan.Matte
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JPanel

fun main() {

    val frame = JFrame().apply {

        val canvas = Canvas(this)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        panel.background = Color(240, 240, 240)
        panel.add(canvas, BorderLayout.CENTER)
        panel.border = Matte()

        contentPane.add(panel)
        pack()

        canvas.requestFocus()

        isResizable = false
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
    }

}