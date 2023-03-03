package com.example.googlemapscompose.data.remote.dto

import com.example.googlemapscompose.domain.model.map.Step
import com.squareup.moshi.Json

data class StepDto(
    val distance: DistanceDto,
    val duration: DurationDto,
    @field:Json(name = "end_location")val endLocation: EndLocationDto?,
    @field:Json(name = "html_instructions") val htmlInstructions: String,
    val maneuver: String?,
    val polyline: PolylineDto,
    @field:Json(name = "start_location") val startLocation: StartLocationDto?,
    @field:Json(name = "travel_mode") val travelMode: String
) {
    fun toStep(): Step {
        return Step(
            startLocation = startLocation,
            endLocation = endLocation,
            polyline = polyline.points
        )
    }
}