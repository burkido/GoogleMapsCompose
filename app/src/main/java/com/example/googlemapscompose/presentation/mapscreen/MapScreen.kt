package com.example.googlemapscompose.presentation.mapscreen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.googlemapscompose.service.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember { MapUiSettings(zoomControlsEnabled = false) }
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(1.35, 103.87),11f) }


    LaunchedEffect(viewModel.locationState.latitude) {
        Log.d("MapScreen", "Location changed: ${viewModel.locationState.latitude}, ${viewModel.locationState.longitude}")
        val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(viewModel.locationState.latitude, viewModel.locationState.longitude), 16f)
        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(MapEvent.ToggleCustomMap) }) {
                Icon(
                    imageVector = if (viewModel.uiState.isCustomMap)
                        Icons.Default.ToggleOff else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Custom View"
                )
            }
        }
    ) {


        val context = LocalContext.current

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = viewModel.uiState.properties,
            uiSettings = uiSettings, // false for prevent overlapping with FAB
            onMapLongClick = { latLng -> viewModel.onEvent(MapEvent.OnMapLongClick(latLng)) },
            onMyLocationButtonClick = {
                Log.d("MapScreen", "MyLocationButton clicked")
                viewModel.onEvent(MapEvent.OnMyLocationButtonClick)
//                Intent(context, LocationService::class.java).apply {
//                    action = LocationService.ACTION_STOP_LOCATION_SERVICE
//                    context.startService(this)
//                }

                true
            },
            cameraPositionState = cameraPositionState
        ) {
            viewModel.uiState.parkingSpots.forEach { parkingSpot ->
                Log.d("MapScreen recomposed", "Location: $parkingSpot")
                Marker(
                    position = LatLng(parkingSpot.latitude, parkingSpot.longitude),
                    title = "Parking Spot (${parkingSpot.latitude}, ${parkingSpot.longitude})",
                    snippet = "Click to remove",
                    onInfoWindowLongClick = {
                        viewModel.onEvent(MapEvent.OnParkingSpotClick(parkingSpot))
                    },
                    onClick = { marker ->
                        marker.showInfoWindow()
                        true
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }


        }
    }
}