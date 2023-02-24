package com.example.googlemapscompose.domain.repository

import com.example.googlemapscompose.domain.model.map.Directions
import com.example.googlemapscompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    fun getRoute(origin: String, destination: String): Flow<Resource<Directions>>
}