package koan

/** Produces a sequence of all pairs of elements in the given list,
 *  irrespective of order, i.e. (a,b) is not distinct from (b,a).
 */
fun <T> allPairs(l: List<T>) = sequence {
	for (i in l.indices) {
		for (j in i + 1 until l.size) {
			yield(Pair(l[i], l[j]))
		}
	}
}