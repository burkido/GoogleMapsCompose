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
        val listOfLatLng = listOf(
            LatLng(33.81607,-117.92253),
            LatLng(33.81593,-117.92262),
            LatLng(33.81586,-117.92268),
            LatLng(33.81586,-117.92268),
            LatLng(33.81577,-117.92277),
            LatLng(33.81573,-117.92283),
            LatLng(33.81569,-117.92290),
            LatLng(33.81564,-117.92299),
            LatLng(33.81562,-117.92308),
            LatLng(33.81560,-117.92315),
            LatLng(33.81559,-117.92323),
            LatLng(33.81559,-117.92330),
            LatLng(33.81559,-117.92336),
            LatLng(33.81558,-117.92359),
            LatLng(33.81558,-117.92384),
            LatLng(33.81558,-117.92406),
        )

        emit(listOfLatLng)
    }

    override fun getSamplePolygon(): Flow<List<Polygon>> = flow {

    }

}