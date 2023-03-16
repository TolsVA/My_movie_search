package com.example.my_movie_search.test

/*import android.icu.text.SimpleDateFormat
import java.util.*

class KotlinClass(var tvInfo: TextView) {
    var ss: String? = "22"

    fun ggg() {
        var d: Double = ss?.toDouble() ?: 1.1

        val numbers = arrayOf(2, 3, "hhh")

        val arr: Array<String?> = Array(10) { "s88" }
        val arr4: Array<String?> = Array(10) { null }
        val arr2 = Array(10) { "s88" }
        val arr3 = arrayOfNulls<String>(10)
        val arr5 = arrayOfNulls<Int>(10)
        val arr6 = arrayOf(10)
        val arr7: ArrayList<Int> = ArrayList(10)
        val arr8 = UIntArray(10)
        arr7.add(88)
        arr7.add(88)
        arr7.add(88)

//        arr4.add(null) // error compilations
        val arr9 = MutableList<String?>(5) { null }
        arr9.add("66")

//        val map: MutableMap<Int, String> = mutableMapOf()
        val map = mutableMapOf<Int, String>()
        map.put(1, "")
        map.put(2, "vvd")
        map[1] = "ddv"
        map[3] = "mmm"

        val movie: MutableList<Movie> = arrayListOf(
            Movie(11, "kk"),
            Movie(22, null),
            Movie(33, ""),
            Movie(44, "kkz")
        )
//        val notEmpty = movie.filter { it.name != null }.filter { it.name != "" }
        val notEmpty = movie.filter { !it.name.isNullOrBlank() }

        tvInfo.text = "${arr4.contentToString()}\n${arr8.contentToString()}\n" +
                "$arr9\n$map\n$notEmpty\n${MainActivity::class.java.simpleName}"
    }

    val sum = { a: Int, b: Int -> a + b }

    fun print(block: () -> Any) {
        println(block())
    }

    val date = Date(1234).format()
//    val dataFormat : String = date.toString()

    val dd: String = date

    class Person {
        var name: String? = null
        var age: Int? = null
    }

    fun fff() {
        val person: Person = Person().also {
            it.name = ""
        }
        with(person) {
            name = "ddd"
            age = 22
        }
    }

    fun getDefaultLocale(deliveryArea: String): Locale {
        val deliverAreaLower = deliveryArea.toLowerCase()
        if (deliverAreaLower == "germany" || deliverAreaLower == "austria") {
            return Locale.GERMAN }
        if (deliverAreaLower == "usa" || deliverAreaLower == "great britain") { return Locale.ENGLISH
        }
        if (deliverAreaLower == "france") {
            return Locale.FRENCH }
        return Locale.ENGLISH }

    fun getDefaultLocale2(deliveryArea: String) = when (deliveryArea.toLowerCase()) {
        "germany", "austria" -> Locale.GERMAN
        "usa", "great britain" -> Locale.ENGLISH
        "france" -> Locale.FRENCH
        else -> Locale.ENGLISH }

}

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"
fun Date.format(): String = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
    .format(this)
*/