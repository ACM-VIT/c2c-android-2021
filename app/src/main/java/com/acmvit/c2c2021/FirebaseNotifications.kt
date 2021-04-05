package com.acmvit.c2c2021

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.acmvit.c2c2021.ui.onboarding.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseNotifications : FirebaseMessagingService() {
    private var mNotificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title: String = ""
        var body: String = ""
        remoteMessage.notification?.let {
            title = it.title.toString()
            body = it.body.toString()
        }
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "0")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_c2c_logo)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        createNotificationChannel()
        mNotificationManager!!.notify(Math.random().toInt(), builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.notification_channel_name)
            val description: String = getString(R.string.notification_channel_disp)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("0", name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.enableVibration(true)
            mNotificationManager = getSystemService(NotificationManager::class.java)
            mNotificationManager!!.createNotificationChannel(channel)
        }
    }

    companion object {
        const val TAG = "FirebaseNotifications"
    }
}