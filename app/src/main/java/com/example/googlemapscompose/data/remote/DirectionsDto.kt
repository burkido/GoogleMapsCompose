package com.example.googlemapscompose.data.remote

import com.example.googlemapscompose.domain.model.map.Directions

data class DirectionsDto(
    val geocodedWaypoints: List<GeocodedWaypointDto>,
    val routes: List<RouteDto>,
    val status: String
) {
    fun toDirections(): Directions {
        return Directions(
            routes = routes.map { it.toRoute() },
            status = status
        )
    }
}