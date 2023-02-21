package com.example.googlemapscompose.presentation.mapscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.repository.LocationClient
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import com.example.googlemapscompose.mapasset.MapStyleGTAV
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val parkingSpotRepository: ParkingSpotRepository,
    private val locationClient: LocationClient,
) : ViewModel() {

    var uiState by mutableStateOf(MapState())

    var locationState by mutableStateOf(LocationState())

    //    val locationJob = locationClient
//        .getLocationUpdates(2000)
//        .catch { e -> e.printStackTrace() }
//        .onEach { location ->
//            locationState = locationState.copy(
//                latitude = location.latitude.toFloat(),
//                longitude = location.longitude.toFloat()
//            )
//        }
//        .launchIn(viewModelScope)
//
    init {
        viewModelScope.launch {
            val locationFlow = locationClient.getLocationUpdates(2000)
            val parkingFlow = parkingSpotRepository.getAllParkingSpots()

            parkingFlow.collect { parkingSpots ->
                Log.d("MapsViewModel", "parkingSpots: $parkingSpots")
                uiState = uiState.copy(parkingSpots = parkingSpots)
            }

            locationFlow.collect { location ->
                Log.d("MapsViewModel", "locationsss: ${location.latitude}, ${location.longitude}")
                locationState = locationState.copy(
                    latitude = location.latitude.toFloat(),
                    longitude = location.longitude.toFloat()
                )
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleCustomMap -> {
                uiState = uiState.copy(
                    properties = uiState.properties.copy(
                        mapStyleOptions = if (uiState.isCustomMap) MapStyleOptions(MapStyleGTAV.json) else null
                    ),
                    isCustomMap = !uiState.isCustomMap
                )
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    Log.d(
                        "MapsViewModel",
                        "clicked location: ${event.latLng.latitude}, ${event.latLng.longitude}"
                    )
                    parkingSpotRepository.insertParkingSpot(
                        ParkingSpot(
                            latitude = event.latLng.latitude,
                            longitude = event.latLng.longitude
                        )
                    )
                }
            }
            is MapEvent.OnParkingSpotClick -> {
                viewModelScope.launch {
                    parkingSpotRepository.deleteParkingSpot(event.parkingSpot)
                }
            }
        }
    }
}