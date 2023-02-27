package com.example.googlemapscompose.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.example.googlemapscompose.domain.repository.LocationClient
import com.example.googlemapscompose.util.hasLocationPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationClient {

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {

            Timber.d("Getting location updates")

            if (!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Location permission not granted")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("You need to enable GPS or Network provider")
            }

            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).apply {
                    setMaxUpdateAgeMillis(interval)
                    setIntervalMillis(interval)
                }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
        }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        if (!context.hasLocationPermission()) {
            onError(LocationClient.LocationException("Location permission not granted"))
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                onSuccess(location)
            } else {
                onError(LocationClient.LocationException("Location is null"))
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    override fun getCurrentLocation(): Location {
//        if (!context.hasLocationPermission())
//            throw LocationClient.LocationException("Location permission not granted")
//
//        var locations: Location? = null
//
//        return fusedLocationProviderClient.getCurrentLocation(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            object : CancellationToken() {
//                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
//                    return CancellationTokenSource().token
//                }
//
//                override fun isCancellationRequested(): Boolean {
//                    return false
//                }
//            }
//        ).addOnSuccessListener { location: Location? ->
//            if (location == null) {
//                Timber.d("Location is null")
//                throw LocationClient.LocationException("Location is null")
//            }
//
//            locations = location
//        }.addOnFailureListener { e ->
//            Timber.d("Error getting current location: ${e.message}")
//            throw LocationClient.LocationException(e.message.toString()) }.result
//
//        //return locations!!
//    }
}