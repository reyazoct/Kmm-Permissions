package tech.kotlinlang.image

sealed class KImageType {
    data class Url(val url: String): KImageType()
}