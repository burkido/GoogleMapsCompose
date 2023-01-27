package com.example.googlemapscompose.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: ParkingSpotRepository,
) : ViewModel() {

    var state by mutableStateOf(MapState())

    init {
        viewModelScope.launch {
            repository.getAllParkingSpots().collectLatest { parkingSpots ->
                state = state.copy(parkingSpots = parkingSpots)
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleCustomMap -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isCustomMap) MapStyleOptions(MapStyleGTAV.json) else null
                    ),
                    isCustomMap = !state.isCustomMap
                )
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    Log.d("MapsViewModel", "clicked location: ${event.latLng.latitude}, ${event.latLng.longitude}")
                    repository.insertParkingSpot(
                        ParkingSpot(
                            latitude = event.latLng.latitude,
                            longitude = event.latLng.longitude
                        )
                    )
                }
            }
            is MapEvent.OnParkingSpotClick -> {
                viewModelScope.launch {
                    repository.deleteParkingSpot(event.parkingSpot)
                }
            }
        }
    }
}