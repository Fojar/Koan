package koan

import java.awt.Color

val WHITE: Color = Color.WHITE
val BLACK: Color = Color.BLACK

val GRAY: Color = Color.GRAY

val RED: Color = Color.RED
val ORANGE: Color = Color.ORANGE
val YELLOW: Color = Color.YELLOW
val GREEN: Color = Color.GREEN
val CYAN: Color = Color.CYAN
val BLUE: Color = Color.BLUE
val MAGENTA: Color = Color.MAGENTA
val PURPLE: Color = Color(.5f, 0f, 1f)


fun rgb(r: Double, g: Double, b: Double) = Color(r.toFloat(), g.toFloat(), b.toFloat())

// Overloads to allow for integer parameters:

fun rgb(r: Int, g: Double, b: Double) = Color(r.toFloat(), g.toFloat(), b.toFloat())
fun rgb(r: Double, g: Int, b: Double) = Color(r.toFloat(), g.toFloat(), b.toFloat())
fun rgb(r: Double, g: Double, b: Int) = Color(r.toFloat(), g.toFloat(), b.toFloat())

fun rgb(r: Int, g: Int, b: Double) = Color(r.toFloat(), g.toFloat(), b.toFloat())
fun rgb(r: Int, g: Double, b: Int) = Color(r.toFloat(), g.toFloat(), b.toFloat())
fun rgb(r: Double, g: Int, b: Int) = Color(r.toFloat(), g.toFloat(), b.toFloat())

fun rgb(r: Int, g: Int, b: Int) = Color(r.toFloat(), g.toFloat(), b.toFloat())



fun hsv(hue: Double, saturation: Double, value: Double) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())

// Overloads to allow for integer parameters:

fun hsv(hue: Int, saturation: Double, value: Double) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())
fun hsv(hue: Double, saturation: Int, value: Double) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())
fun hsv(hue: Double, saturation: Double, value: Int) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())

fun hsv(hue: Int, saturation: Int, value: Double) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())
fun hsv(hue: Int, saturation: Double, value: Int) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())
fun hsv(hue: Double, saturation: Int, value: Int) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())

fun hsv(hue: Int, saturation: Int, value: Int) = Color.getHSBColor(hue.toFloat(), saturation.toFloat(), value.toFloat())