package com.example.googlemapscompose.data.remote.dto

data class GeocodedWaypointDto(
    val geocoder_status: String,
    val partial_match: Boolean,
    val place_id: String,
    val types: List<String>
)