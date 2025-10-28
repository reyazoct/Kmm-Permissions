package tech.kotlinlang.permission.location

class DesktopIPLocationHelper(private val provider: IPLocationProvider) : LocationHelper {
    override suspend fun fetchLastKnownLocation(): LocationRequestResult {
        return try {
            provider.fetchIPLocation()
        } catch (_: Exception) {
            LocationRequestResult.NoLastLocationFound
        }
    }
}
