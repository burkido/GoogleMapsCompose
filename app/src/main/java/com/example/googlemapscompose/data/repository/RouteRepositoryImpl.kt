package com.example.googlemapscompose.data.repository

import com.example.googlemapscompose.domain.model.map.Directions
import com.example.googlemapscompose.domain.repository.RouteRepository
import com.example.googlemapscompose.util.Resource
import kotlinx.coroutines.flow.Flow

class RouteRepositoryImpl(

): RouteRepository {

    override fun getRoute(origin: String, destination: String): Flow<Resource<List<Directions>>> {
        TODO("Not yet implemented")
    }
}