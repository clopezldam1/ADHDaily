package com.example.adhdaily.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotificationHelper(private val context: Context): MainActivity() {
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = context.resources.getString(R.string.notifChannelNameReminders)
            val descriptionText = context.resources.getString(R.string.notifChannelDescReminders)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel =
                NotificationChannel("com.example.adhdaily.reminders", name, importance).apply {
                    description = descriptionText
                    enableLights(true)
                    lightColor = Color.BLUE
                }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun setReminderNotification(reminderDateTime: LocalDateTime){
        val notificationTime: Long = reminderDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        val notificationId = 1

        val notificationHelper = NotificationHelper(context)
        notificationHelper.scheduleNotification(context, notificationTime, notificationId)
    }

     fun createNotification(context: Context): Notification {
        var builder = NotificationCompat.Builder(context, "com.example.adhdaily.reminders")
            .setSmallIcon(R.drawable.app_icon_bg)
            .setColorized(true)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setGroup("reminders")
            //.setGroupSummary(true)

        return builder.build()
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, notificationTime: Long, notificationId: Int) {
        Log.i("notif", "scheduleNotification: notificationTime" + notificationTime)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotifReminderReceiver::class.java)
        intent.putExtra("ADHDailyReminders", notificationId) // Puedes pasar información adicional si es necesario
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent)
    }

}