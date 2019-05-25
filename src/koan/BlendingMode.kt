package koan

import java.awt.*
import java.awt.image.*
import kotlin.math.*


enum class BlendingMode {

    NORMAL {
        override val composite = AlphaComposite.SrcOver
    },
    ADDITIVE {
        override val composite = CustomComposite(::blendAdd)
    },
    SUBTRACTIVE {
        override val composite = CustomComposite(::blendSubtract)
    };

    abstract val composite: Composite
}

class CustomComposite(val blendFunc: (IntArray, IntArray, IntArray) -> Unit) : Composite {

    override fun createContext(
        srcColorModel: ColorModel,
        dstColorModel: ColorModel,
        hints: RenderingHints
    ): CompositeContext {
        return CustomCompositeContext(blendFunc)
    }

}

class CustomCompositeContext(val blendFunc: (IntArray, IntArray, IntArray) -> Unit) : CompositeContext {

    override fun dispose() {}

    private val result = IntArray(4)
    private val srcPixel = IntArray(4)
    private val dstPixel = IntArray(4)

    override fun compose(src: Raster, dstIn: Raster, dstOut: WritableRaster) {
        val width = min(src.width, dstIn.width)
        val height = min(src.height, dstIn.height)

        val srcPixels = IntArray(width)
        val dstPixels = IntArray(width)

        for (y in 0 until height) {
            src.getDataElements(0, y, width, 1, srcPixels)
            dstIn.getDataElements(0, y, width, 1, dstPixels)
            for (x in 0 until width) {

                srcPixels[x].splitRgbToArray(srcPixel)
                dstPixels[x].splitRgbToArray(dstPixel)

                blendFunc(srcPixel, dstPixel, result)

                // Mix the result into the destination using alpha.
                dstPixels[x] = 255 shl 24 or (  // Destination alpha is always 255
                        result[1] and 0xFF shl 16) or (
                        result[2] and 0xFF shl 8) or (
                        result[3] and 0xFF)
            }
            dstOut.setDataElements(0, y, width, 1, dstPixels)
        }

    }

}

// Splits a pixel stored in ARGB form into separate elements of the target array.
private inline fun Int.splitRgbToArray(target: IntArray) {
    target[0] = this shr 24 and 0xFF
    target[1] = this shr 16 and 0xFF
    target[2] = this shr 8 and 0xFF
    target[3] = this and 0xFF
}

private fun blendAdd(src: IntArray, dst: IntArray, result: IntArray) {
    val alpha = src[0]
    result[1] = min(255, src[1] * alpha / 255 + dst[1])
    result[2] = min(255, src[2] * alpha / 255 + dst[2])
    result[3] = min(255, src[3] * alpha / 255 + dst[3])
}

private fun blendSubtract(src: IntArray, dst: IntArray, result: IntArray) {
    val alpha = src[0]
    result[1] = max(0, dst[1] - src[1] * alpha / 255)
    result[2] = max(0, dst[2] - src[2] * alpha / 255)
    result[3] = max(0, dst[3] - src[3] * alpha / 255)
}
