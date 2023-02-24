package com.example.googlemapscompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.googlemapscompose.presentation.mapscreen.MapScreen
import com.example.googlemapscompose.service.LocationService
import com.example.googlemapscompose.ui.theme.GoogleMapsComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            0
        )
        setContent {
            GoogleMapsComposeTheme {
                MapScreen()
//                Column(modifier = Modifier.fillMaxSize()) {
//                    Button(
//                        onClick = {
//                            Intent(applicationContext, LocationService::class.java).apply {
//                                action = LocationService.ACTION_START_LOCATION_SERVICE
//                                startService(this)
//                            }
//                        }) {
//                        Text(text = "Start")
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = {
//                            Intent(applicationContext, LocationService::class.java).apply {
//                                action = LocationService.ACTION_STOP_LOCATION_SERVICE
//                                startService(this)
//                            }
//                        }) {
//                        Text(text = "Stop")
//                    }
//                }
            }
        }
    }
}