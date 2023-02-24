package com.example.googlemapscompose.domain.model.map

import com.example.googlemapscompose.data.remote.DistanceDto
import com.example.googlemapscompose.data.remote.DurationDto

data class Leg(
    val distance: DistanceDto,
    val duration: DurationDto,
    val steps: List<Step>,
)
