package com.example.googlemapscompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParkingSpotEntity(
    @PrimaryKey val id: Int? = null,
    val latitude: Double,
    val lng: Double
)