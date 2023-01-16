package ru.netology.nMediaApp.dto

import java.math.RoundingMode

data class Post (
    val id: Long,
    val autor: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val shareByMe: Boolean,
    val likes: Int,
    val share: Int,
    val urlOfVideo: String? = null
)

object CountService {
    fun countServise (count: Int): String {
        val x = when(count){
            in 0..999 -> count.toString()
            in 1000..1099 -> "1K"
            in 1100..9999 -> when(count % 100){
                in 0..99 -> ((count * 0.001).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()).toString() + "K"
                else -> {"err2"}
            }
            in 10_000..999_999 -> when(count % 1000){
                in 0..999 -> ((count * 0.001).toBigDecimal().setScale(0, RoundingMode.DOWN).toInt()).toString() + "K"
                else -> {"err3"}
            }
                in 1_000_000.. 1_000_000_000 -> when(count % 100){
                    in 0..99 -> ((count * 0.000001).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()).toString() + "M"
                    else -> {"err4"}
                }
            else -> "error"
        }
        return x
    }
}

fun main() {
    println(CountService.countServise(1099))
}
