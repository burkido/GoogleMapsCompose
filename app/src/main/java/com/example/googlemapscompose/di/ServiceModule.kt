package com.example.googlemapscompose.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.googlemapscompose.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun providePendingIntent(
        @ApplicationContext context: Context,
    ): PendingIntent {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                11, // Pending intent request code
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context,
                11, // Pending intent request code
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, "location")
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setContentTitle("Location")
            .setContentText("306, 306")
    }
    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context,
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}