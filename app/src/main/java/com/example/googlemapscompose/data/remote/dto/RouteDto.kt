package com.example.googlemapscompose.data.remote.dto

import android.util.Log
import com.example.googlemapscompose.domain.model.map.Route
import com.squareup.moshi.Json
import timber.log.Timber


data class RouteDto(
    val bounds: BoundsDto,
    val copyrights: String,
    val legs: List<LegDto>?,
    @field:Json(name = "overview_polyline") val overviewPolyline: OverviewPolylineDto,
    val summary: String,
    val warnings: List<Any>,
    val waypoint_order: List<Any>,
) {
    fun toRoute(): Route {
        if (legs == null)
            Timber.d("legDto is null")
        return Route(
            legs = legs?.map { it.toLeg() }
        )
    }
}