package com.example.googlemapscompose.domain.model.map

data class Directions(
    val routes: List<Route>,
    val status: String
)
