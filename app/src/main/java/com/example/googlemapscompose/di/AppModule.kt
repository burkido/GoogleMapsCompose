package com.example.googlemapscompose.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.example.googlemapscompose.data.ParkingSpotDatabase
import com.example.googlemapscompose.data.ParkingSpotImpl
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideParkingSpotDatabase(app: Application): ParkingSpotDatabase {
        return Room.databaseBuilder(
            app,
            ParkingSpotDatabase::class.java,
            "parking_spot_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideParkingSpotRepository(database: ParkingSpotDatabase): ParkingSpotRepository {
        return ParkingSpotImpl(database.parkingSpotDao)
    }

    @Singleton
    @Provides
    fun provideFuseLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}