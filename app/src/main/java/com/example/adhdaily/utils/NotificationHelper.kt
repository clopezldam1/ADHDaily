package com.example.adhdaily.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.metrics.LogSessionId
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.model.entity.Task
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class NotificationHelper(private val context: Context): MainActivity() {

    /**
     * Para actualizar una notificación, cancelamos la existente
     * y creamos una nueva
     */
    fun updateNotification(task: Task, reminderId: Long, oldRemDateTime: LocalDateTime, newRemDateTime: LocalDateTime) {
        cancelNotification(task, reminderId, oldRemDateTime)
        scheduleNotification(task, reminderId, newRemDateTime)
    }

    /**
     * Crear notificación programada para una fecha y hora concreta
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(task: Task, reminderId: Long, remDateTime: LocalDateTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("taskTitle", task.Title)
            putExtra("taskStartTime", task.StartTime)
            putExtra("taskDesc", task.Desc)
            putExtra("remId", reminderId)
            putExtra("taskIconResId", task.ColorTag_FK) // Si tienes un ID de recurso para el icono
        }

        Log.i("notif", "scheduleNotification: last notif ID: " + getLastNotificationId(context))

        val pendingIntent = PendingIntent.getBroadcast(context, reminderId.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val triggerTimeMillis = remDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancelar notificación existente
     */
    fun cancelNotification(task: Task, reminderId: Long, oldRemDateTime: LocalDateTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("taskTitle", task.Title)
            putExtra("taskStartTime", task.StartTime)
            putExtra("taskDesc", task.Desc)
            putExtra("remId", reminderId)
        }

        //TODO: fix - no borra bc no pilla bien el id con el que se crea notif bc es diferente al que tratas de borrar aqui
        val pendingIntent = PendingIntent.getBroadcast(context, reminderId.toInt(), intent,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun getLastNotificationId(context: Context): Int {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNotifications = notificationManager.activeNotifications
            if (activeNotifications.isNotEmpty()) {
                val lastNotification = activeNotifications.last()
                return lastNotification.id
            }
        }
        return -1
    }
}