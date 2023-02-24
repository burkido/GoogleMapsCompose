package com.example.googlemapscompose.di

import com.example.googlemapscompose.data.repository.LocationClientImpl
import com.example.googlemapscompose.domain.repository.LocationClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindLocationClient(locationClientImpl: LocationClientImpl): LocationClient



}