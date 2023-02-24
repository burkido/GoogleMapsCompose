package com.example.googlemapscompose.domain.repository

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import kotlinx.coroutines.flow.Flow

interface ParkingSpotRepository {

    suspend fun insertParkingSpot(parkingSpot: ParkingSpot)

    suspend fun deleteParkingSpot(parkingSpot: ParkingSpot)

    fun getAllParkingSpots(): Flow<List<ParkingSpot>>

    fun getSamplePolyline(): Flow<List<LatLng>>

    fun getSamplePolygon(): Flow<List<Polygon>>
}