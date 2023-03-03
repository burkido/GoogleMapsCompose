package com.example.googlemapscompose.presentation.mapscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googlemapscompose.domain.business.PolylineUtilities
import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.repository.LocationClient
import com.example.googlemapscompose.domain.repository.ParkingSpotRepository
import com.example.googlemapscompose.domain.repository.RouteRepository
import com.example.googlemapscompose.mapasset.MapStyleGTAV
import com.example.googlemapscompose.util.Resource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MapsViewModel @Inject constructor(
    private val parkingSpotRepository: ParkingSpotRepository,
    private val routeRepository: RouteRepository,
    private val locationClient: LocationClient,
    private val polylineUtilities: PolylineUtilities,
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

            val parkingFlow = parkingSpotRepository.getAllParkingSpots()

            parkingFlow.collect { parkingSpots ->
                Timber.d("parkingSpots: $parkingSpots")
                uiState = uiState.copy(parkingSpots = parkingSpots)
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
                    Timber.d("clicked location: " + event.latLng.latitude + ", " + event.latLng.longitude)
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
            MapEvent.OnMyLocationButtonClick -> {
                viewModelScope.launch {
                    locationClient.getCurrentLocation(
                        onSuccess = { location ->
                            Timber.d("current location: " + location.latitude + ", " + location.longitude)
                            locationState = locationState.copy(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        },
                        onError = { e ->
                            Timber.d("error: " + e.message)
                        }
                    )
                }
            }
            MapEvent.OnResetMap -> {
                viewModelScope.launch {
                    uiState = uiState.copy(
                        direction = emptyList(),
                    )
                }
            }
            MapEvent.OnClickDrawRoute -> {
                viewModelScope.launch {
                    val (origin, destination) = "Disneyland" to "Universal+Studios+Hollywood"
                    routeRepository.getRoute(
                        origin = origin,
                        destination = destination
                    ).collect { direction ->
                        Timber.d("direction: $direction")
                        when (direction) {
                            is Resource.Success -> {
                                uiState = uiState.copy(
                                    loading = false,
                                    polylines = listOf(
                                        LatLng(33.81607,-117.92253),
                                        LatLng(33.81593,-117.92262),
                                        LatLng(33.81586,-117.92268),
                                        LatLng(33.81586,-117.92268),
                                        LatLng(33.81577,-117.92277),
                                        LatLng(33.81573,-117.92283),
                                        LatLng(33.81569,-117.92290),
                                        LatLng(33.81564,-117.92299),
                                        LatLng(33.81562,-117.92308),
                                        LatLng(33.81560,-117.92315),
                                        LatLng(33.81559,-117.92323),
                                        LatLng(33.81559,-117.92330),
                                        LatLng(33.81559,-117.92336),
                                        LatLng(33.81558,-117.92359),
                                        LatLng(33.81558,-117.92384),
                                        LatLng(33.81558,-117.92406),
                                    ),
                                )
//                                val steps = direction.data?.routes?.get(0)?.legs?.get(0)?.steps
//                                steps?.forEach {
//                                    Timber.d("stepske: " + it.polyline)
//                                }
//
//                                val routes = steps?.get(0)?.polyline
//                                Timber.d("routes: " + routes)

                            }
                            is Resource.Error -> {
                                uiState = uiState.copy(loading = true)
                                Timber.d("stepske error: " + direction.message)
                            }
                            is Resource.Loading -> {
                                Timber.d("stepske loading")
                                uiState = uiState.copy(loading = true)
                            }
                        }
                    }

                }
            }
        }
    }
}
