package com.example.googlemapscompose.data

import com.example.googlemapscompose.domain.model.ParkingSpot

fun ParkingSpotEntity.toParkingSpot(): ParkingSpot {
    return ParkingSpot(
        id = id,
        latitude = latitude,
        langtitude = lng
    )
}

fun ParkingSpot.toParkingSpotEntity(): ParkingSpotEntity {
    return ParkingSpotEntity(
        id = id,
        latitude = latitude,
        lng = langtitude
    )
}