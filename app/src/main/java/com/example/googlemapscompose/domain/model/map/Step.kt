package com.example.googlemapscompose.domain.model.map

import com.example.googlemapscompose.data.remote.dto.EndLocationDto
import com.example.googlemapscompose.data.remote.dto.StartLocationDto

data class Step(
    val startLocation: StartLocationDto?,
    val endLocation: EndLocationDto?,
)
