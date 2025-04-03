package tech.kotlinlang.permission

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform