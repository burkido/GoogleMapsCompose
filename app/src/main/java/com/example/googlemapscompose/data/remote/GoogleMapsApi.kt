package com.example.googlemapscompose.data.remote

import com.example.googlemapscompose.data.remote.dto.DirectionsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): List<DirectionsDto>

}