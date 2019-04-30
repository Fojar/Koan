package koan


infix fun ClosedRange<Double>.step(step: Int) = this.step(step.toDouble())

infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
    require(start.isFinite())
    require(endInclusive.isFinite())
    require(step > 0.0) { "Step must be positive, was: $step." }
    val sequence = generateSequence(start) { previous ->
        if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
        val next = previous + step
        if (next > endInclusive) null else next
    }
    return sequence.asIterable()
}

operator fun Int.rangeTo(max: Double) = this.toDouble()..max
operator fun Double.rangeTo(max: Int) = this..max.toDouble()

