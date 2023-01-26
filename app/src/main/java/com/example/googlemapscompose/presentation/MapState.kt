package com.example.googlemapscompose.presentation

import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val isCustomMap: Boolean = false
)
