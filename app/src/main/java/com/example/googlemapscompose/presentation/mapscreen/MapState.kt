package com.example.googlemapscompose.presentation.mapscreen

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.model.map.Directions
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(isMyLocationEnabled = true),
    val parkingSpots: List<ParkingSpot> = emptyList(),
    val direction: List<Directions> = emptyList(),
    val isCustomMap: Boolean = false
)

data class LocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
