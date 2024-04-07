package com.example.moengageassignment.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.moengageassignment.MainActivity
import com.example.moengageassignment.R
import com.example.moengageassignment.utils.Utility
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "MangoNews"
    private val channelName = "Mango New Bulletin"


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM", remoteMessage.data.toString())

        // Check if the message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            if (it.imageUrl != null) {
                Thread(Runnable {
                    val bitmap = Utility.getBitmapFromURL(it.imageUrl.toString())
                    showNotificationWithImage(it.title ?: "Data Message", it.body ?: "", bitmap)
                }).start()
            }
            showNotification(it.title ?: "FCM Message", it.body ?: "")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }


    private fun showNotification(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_mango)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent())
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun handleDataMessage(data: Map<String, String>) {
        // Extract title, message, and image URL from the data payload
        val title = data["title"]
        val message = data["message"]
        val imageUrl = data["imageUrl"]

        if (imageUrl != null) {
            // Asynchronously download the image and then show the notification
            Thread(Runnable {
                val bitmap = Utility.getBitmapFromURL(imageUrl)
                showNotificationWithImage(title ?: "Data Message", message ?: "", bitmap)
            }).start()
        } else {
            // Show the notification without an image
            showNotification(title ?: "Data Message", message ?: "")
        }
    }

    private fun showNotificationWithImage(title: String, message: String, bitmap: Bitmap?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_mango)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(getPendingIntent())
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

}
