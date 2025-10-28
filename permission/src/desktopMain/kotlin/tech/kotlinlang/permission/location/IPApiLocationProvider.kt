package tech.kotlinlang.permission.location

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import java.io.IOException

object IPApiLocationProvider : IPLocationProvider {
    private val client = HttpClient()

    override suspend fun fetchIPLocation(): LocationRequestResult.LocationData {
        val latitudeHttpResponse = client.get("https://ipapi.co/latitude")
        if (!latitudeHttpResponse.status.isSuccess()) {
            throw IOException("Failed to fetch IP location latitude: ${latitudeHttpResponse.status}")
        }
        val latitude = latitudeHttpResponse.bodyAsText().toDouble()

        val longitudeHttpResponse = client.get("https://ipapi.co/longitude")
        if (!longitudeHttpResponse.status.isSuccess()) {
            throw IOException("Failed to fetch IP location longitude: ${longitudeHttpResponse.status}")
        }
        val longitude = longitudeHttpResponse.bodyAsText().toDouble()

        return LocationRequestResult.LocationData(latitude, longitude)
    }
}
