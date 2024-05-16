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

    fun cancelNotification(task: Task, reminderId: Long, oldRemDateTime: LocalDateTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("taskTitle", task.Title)
            putExtra("taskStartTime", task.StartTime)
            putExtra("taskDesc", task.Desc)
            putExtra("remId", reminderId)
            putExtra("taskIconResId", task.ColorTag_FK) // Si tienes un ID de recurso para el icono
        }
        val pendingIntent = PendingIntent.getBroadcast(context, reminderId.toInt(), intent,  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Cancelar la alarma existente
        alarmManager.cancel(pendingIntent)

        // Eliminar cualquier instancia pendiente del PendingIntent
        pendingIntent.cancel()
    }

    /*
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
    */

    /*
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, notificationTime: Long, notificationId: Int) {
        Log.i("notif", "scheduleNotification: notificationTime" + notificationTime)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("ADHDailyReminders", notificationId) // Puedes pasar información adicional si es necesario
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent)
    }
     */
}