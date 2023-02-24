package com.example.googlemapscompose.data.remote.dto

import com.example.googlemapscompose.domain.model.map.Step

data class StepDto(
    val distanceDto: DistanceDto,
    val durationDto: DurationDto,
    val endLocationDto: EndLocationDto,
    val htmlInstructions: String,
    val maneuver: String,
    val polylineDto: PolylineDto,
    val startLocationDto: StartLocationDto,
    val travelMode: String
) {
    fun toStep(): Step {
        return Step(
            startLocation = startLocationDto,
            endLocation = endLocationDto
        )
    }
}