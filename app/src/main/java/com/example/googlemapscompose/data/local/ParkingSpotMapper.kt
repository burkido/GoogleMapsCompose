package com.example.googlemapscompose.data

import com.example.googlemapscompose.data.local.entity.ParkingSpotEntity
import com.example.googlemapscompose.domain.model.ParkingSpot

fun ParkingSpotEntity.toParkingSpot(): ParkingSpot {
    return ParkingSpot(
        id = id,
        latitude = latitude,
        longitude = lng
    )
}

fun ParkingSpot.toParkingSpotEntity(): ParkingSpotEntity {
    return ParkingSpotEntity(
        id = id,
        latitude = latitude,
        lng = longitude
    )
}