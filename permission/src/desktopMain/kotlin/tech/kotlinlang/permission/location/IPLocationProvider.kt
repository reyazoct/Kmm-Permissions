package tech.kotlinlang.permission.location

import java.io.IOException

interface IPLocationProvider {
    /**
     * Fetches the geographical location (latitude and longitude) based on the device's IP address.
     * @throws IOException If there's any issue during the network request or data parsing.
     */
    suspend fun fetchIPLocation(): LocationRequestResult.LocationData
}
