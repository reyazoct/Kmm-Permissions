package tech.kotlinlang.permission.location

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import tech.kotlinlang.permission.Permission
import tech.kotlinlang.permission.PermissionHelper
import tech.kotlinlang.permission.PermissionInitiation
import tech.kotlinlang.permission.result.LocationPermissionResult
import kotlin.coroutines.resume

class AndroidLocationHelper(
    private val permissionInitiation: PermissionInitiation,
    private val permissionHelper: PermissionHelper,
) : LocationHelper {
    override suspend fun fetchLastKnownLocation(): LocationRequestResult {
        return withContext(Dispatchers.Main) {
            val permissionResult = permissionHelper.checkIsPermissionGranted(Permission.Location)
            if (permissionResult !is LocationPermissionResult.Granted) {
                return@withContext LocationRequestResult.PermissionFailure(permissionResult)
            }
            val activity = permissionInitiation.getActivity()
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000).build()
            suspendCancellableCoroutine<LocationRequestResult> { continuation ->
                val locationCallBack = object : LocationCallback() {
                    override fun onLocationResult(locationRequestResult: LocationResult) {
                        val location = locationRequestResult.lastLocation ?: run {
                            continuation.resume(LocationRequestResult.NoLastLocationFound)
                            return@onLocationResult
                        }
                        val locationRequestResult = LocationRequestResult.LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                        )
                        continuation.resume(locationRequestResult)
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
                @SuppressLint("MissingPermission")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallBack,
                    Looper.getMainLooper()
                )
            }
        }
    }
}