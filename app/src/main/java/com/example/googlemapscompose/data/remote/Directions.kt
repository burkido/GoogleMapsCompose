package com.example.googlemapscompose.data.remote

data class Directions(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
)