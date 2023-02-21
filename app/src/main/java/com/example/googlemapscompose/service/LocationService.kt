package com.example.googlemapscompose.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.googlemapscompose.R
import com.example.googlemapscompose.data.LocationClientImpl
import com.example.googlemapscompose.domain.repository.LocationClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    @Inject
    lateinit var notification: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        locationClient = LocationClientImpl(
            context = applicationContext,
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START_LOCATION_SERVICE -> {
                start()
                createNotificationChannel()
            }
            ACTION_STOP_LOCATION_SERVICE -> { stop() }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun start() {
//        val notification = NotificationCompat.Builder(this, "location")
//            .setContentTitle("Tracking location...")
//            .setContentText("Location:")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setOngoing(true)

        //val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(4000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)    // STOP_FOREGROUND_DETACH
        stopSelf()
    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        when(intent?.action) {
//            ACTION_START_LOCATION_SERVICE -> {
//                serviceScope.launch {
//                    locationClient.getLocationUpdates(1000L)
//                }
//            }
//            ACTION_STOP_LOCATION_SERVICE -> {
//                stopSelf()
//            }
//        }
//
//        return super.onStartCommand(intent, flags, startId)
//    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val TAG = "LocationService"
        const val ACTION_START_LOCATION_SERVICE = "ACTION_START_LOCATION_SERVICE"
        const val ACTION_STOP_LOCATION_SERVICE = "ACTION_STOP_LOCATION_SERVICE"
    }
}