package com.example.googlemapscompose.presentation.mapscreen

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(isMyLocationEnabled = true),
    val parkingSpots: List<ParkingSpot> = emptyList(),
    val isCustomMap: Boolean = false
)

data class LocationState(
    val latitude: Float = 0.0f,
    val longitude: Float = 0.0f,
)
