package koan

import koan.gui.ConfigView
import koan.gui.DrawingTreeView
import java.awt.*
import java.awt.event.ActionEvent
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.*
import javax.swing.BoxLayout


private val consoleTextArea: JTextArea = JTextArea(8, 50).apply {
	background = Color.DARK_GRAY
	foreground = Color.WHITE
	font = Font(Font.MONOSPACED, Font.PLAIN, 15)
}

fun clearConsole() {
	consoleTextArea.text = ""
}

fun main() {

	System.setOut(PrintStream(object : OutputStream() {
		override fun write(b: Int) {
			consoleTextArea.append(b.toChar().toString())
		}
	}))

	val canvas: Canvas
	val matte: Matte

	val frame = JFrame().apply {

		canvas = Canvas(this)
		matte = Matte()

		val panel = JPanel().apply {
			layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
			background = Color(220, 220, 220)
			add(canvas, BorderLayout.CENTER)
			border = matte
		}

		contentPane.add(panel)
		pack()

		canvas.requestFocus()

		isResizable = false
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	}

	val controlPanel = JFrame().apply {

		val drawingBrowser = JPanel().apply {

			val drawingTree = DrawingTreeView {
				canvas.loadDrawing(it)
			}

			add(Box.createVerticalStrut(5))
			addButton("Refresh directories") {
				drawingTree.refreshModel()
			}

			add(Box.createVerticalStrut(5))
			add(JLabel("Select drawing:"))

			add(drawingTree)
			layout = BoxLayout(this, BoxLayout.Y_AXIS)
			preferredSize = Dimension(250, 400)
		}

		val config = ConfigView({ scaleFactor ->
			matte.updateScale(scaleFactor)
			canvas.scaleFactor = scaleFactor
		}, { resolutionMultiplier ->
			Drawing.resolutionMultiplier = resolutionMultiplier
			canvas.reloadDrawing()
		})


		val tabPane = JTabbedPane().apply {
			addTab("Browser", drawingBrowser)
			addTab("Config", config)
		}

		contentPane.add(tabPane, BorderLayout.CENTER)

		val buttonsPanel = JPanel().apply {
			layout = GridLayout(4, 1)

			addButton("<html><center><h2>Reload</h2>After code is changed</center></html>") {
				canvas.reloadDrawing()
			}
			addButton("<html><center><h2>Reset</h2>Calls reset and draw</center></html>") {
				canvas.resetDrawing()
			}
			addButton("<html><center><h2>Draw</h2>Only calls draw</center></html>") {
				canvas.drawDrawing()
			}
			addButton("<html><center><h2>Save Image</h2>Into gallery folder</center></html>") {
				canvas.saveDrawing()
			}
			preferredSize = Dimension(200, 0)
		}
		contentPane.add(buttonsPanel, BorderLayout.EAST)

		val consolePanel = JPanel().apply {
			add(JScrollPane(consoleTextArea))
		}
		contentPane.add(consolePanel, BorderLayout.PAGE_END)

		pack()
		title = "Koan Control Panel"
		isResizable = false
		defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		isVisible = true
	}

	with(frame) {
		location = java.awt.Point(controlPanel.width, 0)
		isVisible = true
	}

}

private fun JPanel.addButton(title: String, clickHandler: (ActionEvent) -> Unit) {
	add(JButton(title).apply {
		addActionListener(clickHandler)
	})
}
