package com.example.googlemapscompose.data.remote.dto

import com.example.googlemapscompose.domain.model.map.Directions
import com.squareup.moshi.Json

data class DirectionsDto(
    @field:Json(name = "geocoded_waypoints") val geocodedWaypoints: List<GeocodedWaypointDto>,
    val routes: List<RouteDto>?,
    val status: String
) {
    fun toDirections(): Directions {
        return Directions(
            routes = routes?.map { it.toRoute() },
            status = status
        )
    }
}