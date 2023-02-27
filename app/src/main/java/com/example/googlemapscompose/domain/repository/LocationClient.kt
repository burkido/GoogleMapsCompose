package com.example.googlemapscompose.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocationUpdates(interval: Long): Flow<Location>

    fun getCurrentLocation(onSuccess: (Location) -> Unit, onError: (Exception) -> Unit): Unit

    class LocationException(message: String): Exception(message)
}