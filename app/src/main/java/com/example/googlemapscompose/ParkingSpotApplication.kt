package com.example.googlemapscompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ParkingSpotApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initApp()
    }

    private fun initApp() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}