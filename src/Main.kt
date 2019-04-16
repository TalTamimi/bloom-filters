import java.io.File
import kotlin.math.abs
import kotlin.random.Random
import kotlin.streams.toList


fun main() {

    val rawlist = File("wordlist.txt").readLines()
    val removedWords = rawlist.take(2500)
    val dic = rawlist.takeLast(rawlist.size - 2500)

    val filterer = bloomFilter(dic, 10000000)



    println(removedWords.filter(filterer).count())


}

fun bloomFilter(words: List<String>, n: Int): (String) -> Boolean {

    val bloom = MutableList(n) { false }

    val hash = { x: String -> abs(x.hashCode()) % n }
    val hash2 = { x: String ->
        val teez = x.chars().toList()
            .mapIndexed { value, i ->
                value * i * 1000081
            }
            .sum()



        abs(teez) % n
    }


    words.forEach {
        bloom[hash(it)] = true
        bloom[hashRNG(it.toByteArray(), n, 1)[0]] = true

    }


    println("shawerma " + bloom.size)
    println("shawerma " + bloom.filter { it }.count())
    println("shawerma " + bloom.size / bloom.filter { it }.count())

    return { newW: String ->
        bloom[hashRNG(newW.toByteArray(), n, 1)[0]]
                &&
                bloom[hash(newW)]
    }

}

fun hash3(str: String, n: Int): Int {
    var hash = 0
    for (i in 0 until str.length) {
        hash = str[i].toInt() + ((hash shl 5) - hash)
    }
    return abs(hash) % n
}

fun hashRNG(value: ByteArray, m: Int, k: Int): IntArray {
    val positions = IntArray(k)
    val r = Random(hashBytes(value))
    for (i in 0 until k) {
        positions[i] = r.nextInt(m)
    }
    return positions
}


fun hashBytes(a: ByteArray?): Int {
    // 32 bit FNV constants. Using longs as Java does not support unsigned
    // datatypes.
    val FNV_PRIME: Long = 16777619
    val FNV_OFFSET_BASIS = 2166136261L

    if (a == null)
        return 0

    var result = FNV_OFFSET_BASIS
    for (element in a) {
        result = result * FNV_PRIME and -0x1
        result = result xor element.toLong()
    }

    // return Arrays.hashCode(a);
    return result.toInt()
}