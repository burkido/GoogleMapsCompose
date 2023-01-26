package com.example.googlemapscompose.domain.repository

import com.example.googlemapscompose.domain.model.ParkingSpot
import kotlinx.coroutines.flow.Flow

interface ParkingSpotRepository {

    suspend fun insertParkingSpot(parkingSpot: ParkingSpot)

    suspend fun deleteParkingSpot(parkingSpot: ParkingSpot)

    fun getAllParkingSpots(): Flow<List<ParkingSpot>>
}