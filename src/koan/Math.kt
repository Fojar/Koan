package koan

import kotlin.math.*


const val π = PI
const val TAU = PI * 2
const val τ = TAU

typealias Real = Double


fun angleToPoint(θ: Double) = Point(cos(θ), sin(θ))
fun angleToPoint(θ: Int) = Point(cos(θ.toDouble()), sin(θ.toDouble()))


fun ClosedRange<Double>.lerp(t: Double) = start + (endInclusive - start) * t
fun ClosedRange<Double>.lerp(t: Int) = start + (endInclusive - start) * t


// Creates a function that generates a particular wave.
fun SineWave(cycleScale: Double, amplitude: Double, phase: Double = 0.0): (Double) -> Double {
	return { sin(phase + cycleScale * it) * amplitude }
}

// Functions for defining the cycle of a wave.
fun frequency(f: Double) = τ * f

fun frequency(f: Int) = frequency(f.toDouble())
fun wavelength(λ: Double) = τ / λ
fun wavelength(λ: Int) = wavelength(λ.toDouble())

// Creates a function that yields the sum of the outputs of several other functions.
fun sum(vararg funcs: (Double) -> Double): (Double) -> Double {
	return { z: Double -> funcs.sumByDouble { wf -> wf(z) } }
}

// This version of the SineWave takes a lambda for the phase parameter, allowing it to be animated over time.
fun PhasingSineWave(cycleScaleFunc: Double, amplitude: Double, phaseFunc: (Double) -> Double): (Double) -> Double {
	return { sin(phaseFunc(it) + cycleScaleFunc * it) * amplitude }
}

// Creates a Gaussain function with the given parameters and a height of 1.
fun Gaussian(centre: Double, deviation: Double) =
	{ x: Double -> exp(-(x - centre).pow(2) / (2 * deviation.pow(2))) }

fun Int.pow(x: Int) = this.toDouble().pow(x)
fun Int.pow(x: Double) = this.toDouble().pow(x)
