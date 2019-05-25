package koan

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextULong

var randomSeed: ULong = Random.Default.nextULong()
    set (newSeed) {
        field = newSeed
        RNG = Random(newSeed.toLong())
        val timestamp = LocalDateTime.now().format(dateTimeFormatter)
        seedLog.appendText("$timestamp ~ $newSeed" + System.lineSeparator())
    }

private var RNG: Random = Random(randomSeed.toLong())

private val seedLog = File("seed.log")
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")

fun reseedRNG() {
    randomSeed = Random.Default.nextULong()
}


fun rand() = RNG.nextDouble()

fun rand(max: Double) = RNG.nextDouble(max)
fun rand(max: Int) = RNG.nextDouble(max.toDouble())

fun rand(p: Point) = Point(rand(p.x), rand(p.y))

fun rand(min: Double, max: Double) = RNG.nextDouble(min, max)
fun rand(min: Int, max: Int) = RNG.nextDouble(min.toDouble(), max.toDouble())
fun rand(min: Int, max: Double) = RNG.nextDouble(min.toDouble(), max)
fun rand(min: Double, max: Int) = RNG.nextDouble(min, max.toDouble())

fun rint(until: Int) = RNG.nextInt(until)
fun rint(from: Int, until: Int) = RNG.nextInt(from, until)
fun rint(range: IntRange) = RNG.nextInt(range)
