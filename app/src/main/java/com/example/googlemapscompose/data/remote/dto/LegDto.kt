package com.example.googlemapscompose.data.remote.dto

import com.example.googlemapscompose.domain.model.map.Leg

data class LegDto(
    val distance: DistanceDto,
    val duration: DurationDto,
    val end_address: String,
    val end_location: EndLocationDto?,
    val start_address: String,
    val start_location: StartLocationDto,
    val steps: List<StepDto>?,
    val traffic_speed_entry: List<Any>,
    val via_waypoint: List<Any>
) {
    fun toLeg(): Leg {
        return Leg(
            distance = distance,
            duration = duration,
            steps = steps?.map { it.toStep() }
        )
    }
}