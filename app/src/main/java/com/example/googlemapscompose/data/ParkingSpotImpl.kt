package com.example.googlemapscompose.data


import android.util.Log
import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParkingSpotImpl(
    private val parkingSpotDao: ParkingSpotDao
): ParkingSpotRepository {

    override suspend fun insertParkingSpot(parkingSpot: ParkingSpot) {
        Log.d("ParkingSpotImpl", "Inserting location: ${parkingSpot.latitude}, ${parkingSpot.longitude}")
        parkingSpotDao.insertParkingSpot(parkingSpot.toParkingSpotEntity())
    }

    override suspend fun deleteParkingSpot(parkingSpot: ParkingSpot) {
        parkingSpotDao.deleteParkingSpot(parkingSpot.toParkingSpotEntity())
    }

    override fun getAllParkingSpots(): Flow<List<ParkingSpot>> {
        return parkingSpotDao.getAllParkingSpots().map { parkingSpots ->
            parkingSpots.map { it.toParkingSpot() }
        }
    }
}