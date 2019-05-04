package koan

import kotlin.math.*

const val TAU = PI * 2


fun ClosedRange<Double>.lerp(t: Double) = start + (endInclusive - start) * t
fun ClosedRange<Double>.lerp(t: Int) = start + (endInclusive - start) * t
