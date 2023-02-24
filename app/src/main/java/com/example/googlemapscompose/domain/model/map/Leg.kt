package com.example.googlemapscompose.domain.model.map

import com.example.googlemapscompose.data.remote.dto.DistanceDto
import com.example.googlemapscompose.data.remote.dto.DurationDto

data class Leg(
    val distance: DistanceDto,
    val duration: DurationDto,
    val steps: List<Step>?,
)
