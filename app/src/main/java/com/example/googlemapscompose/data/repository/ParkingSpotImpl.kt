package com.example.googlemapscompose.data.repository

import com.example.googlemapscompose.data.local.ParkingSpotDao
import com.example.googlemapscompose.data.toParkingSpot
import com.example.googlemapscompose.data.toParkingSpotEntity
import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class ParkingSpotImpl(
    private val parkingSpotDao: ParkingSpotDao
): ParkingSpotRepository {

    override suspend fun insertParkingSpot(parkingSpot: ParkingSpot) {
        Timber.d("Inserting location: " + parkingSpot.latitude + ", " + parkingSpot.longitude)
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

    override fun getSamplePolyline(): Flow<List<LatLng>> = flow {

    }

    override fun getSamplePolygon(): Flow<List<Polygon>> = flow {

    }

}