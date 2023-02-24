package com.example.googlemapscompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googlemapscompose.data.local.ParkingSpotDao
import com.example.googlemapscompose.data.local.entity.ParkingSpotEntity

@Database(
    entities = [ParkingSpotEntity::class],
    version = 1,
)
abstract class ParkingSpotDatabase: RoomDatabase() {

    abstract val parkingSpotDao: ParkingSpotDao
}