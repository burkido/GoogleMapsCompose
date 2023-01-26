package com.example.googlemapscompose.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ParkingSpotEntity::class],
    version = 1,
)
abstract class ParkingSpotDatabase: RoomDatabase() {

    abstract val parkingSpotDao: ParkingSpotDao
}