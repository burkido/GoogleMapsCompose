package com.example.googlemapscompose.presentation.mapscreen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker

@Composable
fun MapScreen(
    viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(MapEvent.ToggleCustomMap) }) {
                Icon(
                    imageVector = if (viewModel.state.isCustomMap)
                        Icons.Default.ToggleOff else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Custom View"
                )
            }
        }
    ) {




        LaunchedEffect(key1 = , )
        
        
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = viewModel.state.properties,
            uiSettings = uiSettings, // false for prevent overlapping with FAB
            onMapLongClick = { latLng ->
                viewModel.onEvent(MapEvent.OnMapLongClick(latLng))
            },
        ) {
            viewModel.state.parkingSpots.forEach { parkingSpot ->
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