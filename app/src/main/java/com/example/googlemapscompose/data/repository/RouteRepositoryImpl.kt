package com.example.googlemapscompose.data.repository

import com.example.googlemapscompose.data.remote.GoogleMapsApi
import com.example.googlemapscompose.domain.model.map.Directions
import com.example.googlemapscompose.domain.repository.RouteRepository
import com.example.googlemapscompose.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RouteRepositoryImpl(
    private val googleMapsApi: GoogleMapsApi
): RouteRepository {

    override fun getRoute(origin: String, destination: String): Flow<Resource<List<Directions>>> = flow {
        emit(Resource.Loading())

        try {
            val directions = googleMapsApi.getDirections(origin = origin, destination = destination, key = "AIzaSyCJLIgC1QjjevZfcHt0ynMPs_1WSK8vi4M")
            emit(Resource.Success(data = directions.map { it.toDirections() }))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "An unknown error occured"))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "An unknown error occured"))
        }
    }
}