package com.example.googlemapscompose.presentation.mapscreen

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleCustomMap: MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnParkingSpotClick(val parkingSpot: ParkingSpot): MapEvent()

    object OnMyLocationButtonClick: MapEvent()
}
