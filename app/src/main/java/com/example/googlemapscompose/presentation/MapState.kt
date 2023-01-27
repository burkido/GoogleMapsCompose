package com.example.googlemapscompose.presentation

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val parkingSpots: List<ParkingSpot> = emptyList(),
    val isCustomMap: Boolean = false
)
