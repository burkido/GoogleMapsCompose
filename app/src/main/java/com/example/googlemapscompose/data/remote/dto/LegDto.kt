package com.example.googlemapscompose.data.remote.dto

import com.example.googlemapscompose.domain.model.map.Leg

data class LegDto(
    val distanceDto: DistanceDto,
    val durationDto: DurationDto,
    val end_address: String,
    val end_location: EndLocationDto,
    val start_address: String,
    val start_location: StartLocationDto,
    val stepDtos: List<StepDto>,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
) {
    fun toLeg(): Leg {
        return Leg(
            distance = distanceDto,
            duration = durationDto,
            steps = stepDtos.map { it.toStep() }
        )
    }
}