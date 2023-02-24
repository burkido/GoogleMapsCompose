package com.example.googlemapscompose.domain.model.map

import com.example.googlemapscompose.data.remote.EndLocationDto
import com.example.googlemapscompose.data.remote.StartLocationDto

data class Step(
    val startLocation: StartLocationDto,
    val endLocation: EndLocationDto,
)
