package com.example.adhdaily.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotifReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context , intent: Intent?) {
        val notificationHelper = NotificationHelper(context)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val notification = notificationHelper.createNotification(context)

        notificationManager.notify(notificationId, notification)
    }

}
