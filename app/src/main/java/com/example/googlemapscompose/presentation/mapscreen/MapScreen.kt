package com.example.googlemapscompose.presentation.mapscreen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.DeleteOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.googlemapscompose.service.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
fun MapScreen(
    //viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {

    val viewModel: MapsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    //val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    //val bottomSheetState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val scaffoldState = rememberScaffoldState()
    val uiSettings = remember { MapUiSettings() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(33.8160897, -117.9225226), 20f)
    }
    val scope = rememberCoroutineScope()

    Timber.d("Inside Map Screen")

    LaunchedEffect(viewModel.locationState.latitude) {
        Timber.d("Location changed: " + viewModel.locationState.latitude + ", " + viewModel.locationState.longitude)
        val cameraPosition = CameraPosition.fromLatLngZoom(
            LatLng(
                viewModel.locationState.latitude,
                viewModel.locationState.longitude
            ), 16f
        )
        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Row(
                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
                    .padding(16.dp)
                    .testTag("BottomSheet")
            ) {

            }
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Google Maps Compose",
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.onEvent(MapEvent.OnClickDrawRoute) }) {
                            Icon(
                                imageVector = Icons.Default.AddRoad,
                                contentDescription = "Draw Route"
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(MapEvent.OnResetMap) }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Reset Map"
                            )
                        }
                        IconButton(onClick = {
                            scope.launch { bottomSheetState.bottomSheetState.apply { if (isCollapsed) expand() else collapse() } }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.onEvent(MapEvent.ToggleCustomMap) }) {
                    Icon(
                        imageVector = if (viewModel.uiState.isCustomMap)
                            Icons.Default.ToggleOff else Icons.Default.ToggleOn,
                        contentDescription = "Toggle Custom View"
                    )
                }
            }
        ) {

            val context = LocalContext.current
            val uiState = viewModel.uiState

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = viewModel.uiState.properties,
                uiSettings = uiSettings, // false for prevent overlapping with FAB
                onMapLongClick = { latLng -> viewModel.onEvent(MapEvent.OnMapLongClick(latLng)) },
                onMyLocationButtonClick = {
                    Timber.d("MyLocationButton clicked")
                    viewModel.onEvent(MapEvent.OnMyLocationButtonClick)
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP_LOCATION_SERVICE
                        context.startService(this)
                    }
                    true
                },
                cameraPositionState = cameraPositionState
            ) {

                Timber.d("Inside Google Map")

                uiState.parkingSpots.forEach { parkingSpot ->
                    Timber.tag("MapScreen recomposed").d("Location: %s", parkingSpot)
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

                DrawPolylines(
                    polylines = uiState.polylines,
                    jointType = JointType.ROUND,
                    width = 10f
                )
            }
        }
    }
}

@Composable
fun ControlMapButtons(
    onResetMap: () -> Unit,
    onDrawRoute: () -> Unit,
    onDrawUI: () -> Unit,
) {
    Row {
        MapButton(
            text = "Reset map",
            onClick = onResetMap,
            modifier = Modifier.testTag("reset_map_button")
        )
        MapButton(
            text = "Draw route",
            onClick = onDrawRoute
        )
        MapButton(
            text = "Display UI",
            onClick = onDrawUI
        )
    }
}

@Composable
fun DrawPolylines(
    polylines: List<LatLng>,
    jointType: Int,
    width: Float,
) {

    Timber.d("Inside Draw Polylines")

    Polyline(
        points = polylines,
        jointType = jointType,
        color = Color(Color.Black.hashCode()),
        width = width,
    )
}

@Composable
fun MapButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        onClick = { onClick() }
    ) {
        Text(text = text, style = MaterialTheme.typography.body1)
    }

}
