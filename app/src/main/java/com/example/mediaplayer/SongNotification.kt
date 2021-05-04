package com.example.mediaplayer

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SongNotification(private var activity:Activity, private var songTitle: String, private var songImage: Int) {
    companion object {
        private const val NOTIFICATION_ID = 1010234
        private const val CHANNEL_ID = "channelID"
    }
    private var builder: NotificationCompat.Builder
    init {
        builder = NotificationCompat.Builder(activity.applicationContext, CHANNEL_ID)
                .setSmallIcon(songImage)
                .setContentTitle(songTitle)
                .setContentText("00:00")
                .setAutoCancel(true).setNotificationSilent()
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
    }
    private fun notifySong(){
        with(NotificationManagerCompat.from(activity.applicationContext)) {
            notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
        }
    }
    fun updateTime(mins: Int, secs: Int){
        if(secs < 10) {
            builder.setContentText("0$mins:0$secs")
        } else {
            builder.setContentText("0$mins:$secs")
        }
        notifySong()
    }



    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}