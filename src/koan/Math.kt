package koan

import kotlin.math.*

const val TAU = PI * 2


fun ClosedRange<Double>.lerp(t: Double) = start + (endInclusive - start) * t
fun ClosedRange<Double>.lerp(t: Int) = start + (endInclusive - start) * t


// Creates a function that generates a particular wave.
fun SineWave(cycleScale: Double, amplitude: Double, phase: Double = 0.0): (Double) -> Double {
    return { sin(phase + cycleScale * it) * amplitude }
}

// Functions for defining the cycle of a wave.
fun frequency(f: Double) = TAU * f

fun wavelength(λ: Double) = TAU / λ

// Creates a function that yields the sum of the outputs of several wave functions.
fun sum(vararg waveFuncs: (Double) -> Double): (Double) -> Double {
    return { z: Double -> waveFuncs.sumByDouble { wf -> wf(z) } }
}