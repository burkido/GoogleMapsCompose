package com.example.googlemapscompose.presentation.mapscreen

import com.example.googlemapscompose.domain.model.ParkingSpot
import com.example.googlemapscompose.domain.model.map.Directions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.MapProperties

/*
* <select id="locations" onchange="highlight(this.selectedIndex)" ondblclick="jumpToLocation()" size="14">
* <option value="0">(33.81607,-117.92253) Level: 3</option>
* <option value="1">(33.81593,-117.92262) Level: 3</option>
* <option value="2">(33.81586,-117.92268) Level: 3</option>
* <option value="3">(33.81586,-117.92268) Level: 3</option>
* <option value="4">(33.81577,-117.92277) Level: 3</option>
* <option value="5">(33.81573,-117.92283) Level: 3</option>
* <option value="6">(33.81569,-117.92290) Level: 3</option>
* <option value="7">(33.81564,-117.92299) Level: 3</option>
* <option value="8">(33.81562,-117.92308) Level: 3</option>
* <option value="9">(33.81560,-117.92315) Level: 3</option>
* <option value="10">(33.81559,-117.92323) Level: 3</option
* ><option value="11">(33.81559,-117.92330) Level: 3</option>
* <option value="12">(33.81559,-117.92336) Level: 3</option>
* <option value="13">(33.81558,-117.92359) Level: 3</option>
* <option value="14">(33.81558,-117.92384) Level: 3</option>
* <option value="15">(33.81558,-117.92406) Level: 3</option></select>
* */

data class MapState(
    val properties: MapProperties = MapProperties(isMyLocationEnabled = true),
    val parkingSpots: List<ParkingSpot> = emptyList(),
    val polylines: List<LatLng> = emptyList(),
//    val polylines: List<LatLng> = listOf(
//        LatLng(33.81607,-117.92253),
//        LatLng(33.81593,-117.92262),
//        LatLng(33.81586,-117.92268),
//        LatLng(33.81586,-117.92268),
//        LatLng(33.81577,-117.92277),
//        LatLng(33.81573,-117.92283),
//        LatLng(33.81569,-117.92290),
//        LatLng(33.81564,-117.92299),
//        LatLng(33.81562,-117.92308),
//        LatLng(33.81560,-117.92315),
//        LatLng(33.81559,-117.92323),
//        LatLng(33.81559,-117.92330),
//        LatLng(33.81559,-117.92336),
//        LatLng(33.81558,-117.92359),
//        LatLng(33.81558,-117.92384),
//        LatLng(33.81558,-117.92406),
//    ),
    val direction: List<Directions> = emptyList(),
    val isCustomMap: Boolean = false,
    val loading: Boolean = false
)

data class LocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
