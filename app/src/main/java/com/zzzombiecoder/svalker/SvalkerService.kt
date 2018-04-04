@file:Suppress("NOTHING_TO_INLINE")

package com.zzzombiecoder.svalker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


class SvalkerService : Service() {

    private val manager: NotificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val managerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private inline fun createNotification() {
        createChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, "STALKER")
                .setSmallIcon(R.drawable.ic_wb_sunny)
                .setContentText("КПК Зона")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build()
        managerCompat.notify(NOTIFICATION_ID.hashCode(), notification)
    }

    private inline fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_MIN
            val mChannel = NotificationChannel(CHANNEL_ID, "Svalker", importance)
            manager.createNotificationChannel(mChannel)
        }
    }
}

private const val CHANNEL_ID = "SVALKER_ID"
private const val NOTIFICATION_ID = "SVALKER_NOTIFICATION_ID"