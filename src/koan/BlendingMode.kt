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

    private val result = IntArray(3)
    private val srcPixel = IntArray(3)
    private val dstPixel = IntArray(3)

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
                val alpha = srcPixels[x] shr 24 and 0xFF

                blendFunc(srcPixel, dstPixel, result)

                // Mix the result into the destination using alpha.
                dstPixels[x] = 255 shl 24 or (  // Destination alpha is always 255
                        mixComponent(dstPixel[0], result[0], alpha) and 0xFF shl 16) or (
                        mixComponent(dstPixel[1], result[1], alpha) and 0xFF shl 8) or (
                        mixComponent(dstPixel[2], result[2], alpha) and 0xFF)
            }
            dstOut.setDataElements(0, y, width, 1, dstPixels)
        }

    }

}

// Splits a pixel stored in ARGB form into separate RGB elements of the target array.
private inline fun Int.splitRgbToArray(target: IntArray) {
    target[0] = this shr 16 and 0xFF
    target[1] = this shr 8 and 0xFF
    target[2] = this and 0xFF
}

private inline fun mixComponent(dst: Int, result: Int, alpha: Int) = dst + (result - dst) * alpha / 255


private fun blendAdd(src: IntArray, dst: IntArray, result: IntArray) {
    result[0] = min(255, src[0] + dst[0])
    result[1] = min(255, src[1] + dst[1])
    result[2] = min(255, src[2] + dst[2])
}

private fun blendSubtract(src: IntArray, dst: IntArray, result: IntArray) {
    result[0] = max(0, dst[0] - src[0])
    result[1] = max(0, dst[1] - src[1])
    result[2] = max(0, dst[2] - src[2])
}
