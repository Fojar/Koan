package koan.gui

import koan.Real
import java.awt.Component
import javax.swing.*

class ConfigView(
	scaleChangedHandler: (Real) -> Unit,
	resolutionChangedHandler: (Int) -> Unit
) : JPanel() {

	private var scaleFactorPercent = 100

	val scaleFactor: Real get() = scaleFactorPercent * .01

	var resolutionMultiplier: Int = 1
		private set


	init {
		layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

		add(JLabel("Canvas scale (%)", JLabel.CENTER).apply {
			alignmentX = Component.CENTER_ALIGNMENT
		})

		add(Box.createVerticalStrut(5))

		add(JSlider(JSlider.HORIZONTAL, 20, 100, scaleFactorPercent).apply {
			majorTickSpacing = 20
			minorTickSpacing = 5
			paintTicks = true
			paintLabels = true
			snapToTicks = true
			extent = 5
			addChangeListener {
				val newScaleFactorPercent = (value + 3) / 5 * 5
				if (scaleFactorPercent != newScaleFactorPercent) {
					scaleFactorPercent = newScaleFactorPercent
					scaleChangedHandler(scaleFactor)
				}
			}

		})

		add(Box.createVerticalStrut(20))

		add(JLabel("Resolution multiplier", JLabel.CENTER).apply {
			alignmentX = Component.CENTER_ALIGNMENT
		})

		add(Box.createVerticalStrut(5))

		add(JSlider(JSlider.HORIZONTAL, 1, 4, 1).apply {
			majorTickSpacing = 1
			paintTicks = true
			paintLabels = true
			snapToTicks = true
			addChangeListener {
				if (value != resolutionMultiplier) {
					resolutionMultiplier = value
					resolutionChangedHandler(resolutionMultiplier)
				}
			}

		})

	}

}

