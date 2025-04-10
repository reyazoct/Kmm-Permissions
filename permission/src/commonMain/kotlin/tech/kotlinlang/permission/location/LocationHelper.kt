package tech.kotlinlang.permission.location

interface LocationHelper {
    suspend fun fetchLastKnownLocation(): LocationRequestResult
}