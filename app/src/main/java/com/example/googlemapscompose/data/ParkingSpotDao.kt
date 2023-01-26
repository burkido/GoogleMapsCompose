package com.example.googlemapscompose.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface ParkingSpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)    // replace with the new one if the row already exists
    suspend fun insertParkingSpot(parkingSpotEntity: ParkingSpotEntity)

    @Delete
    suspend fun deleteParkingSpot(parkingSpotEntity: ParkingSpotEntity)

    @Query("SELECT * FROM parkingspotentity")
    fun getAllParkingSpots(): Flow<List<ParkingSpotEntity>>

}