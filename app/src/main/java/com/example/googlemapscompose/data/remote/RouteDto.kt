package com.example.googlemapscompose.data.remote

import com.example.googlemapscompose.domain.model.map.Route


data class RouteDto(
    val boundsDto: BoundsDto,
    val copyrights: String,
    val legDto: List<LegDto>,
    val overviewPolyline: OverviewPolylineDto,
    val summary: String,
    val warnings: List<Any>,
    val waypoint_order: List<Any>
) {
    fun toRoute(): Route {
        return Route(
            legs = legDto.map { it.toLeg() }
        )
    }
}