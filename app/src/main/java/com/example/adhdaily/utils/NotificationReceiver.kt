package com.example.adhdaily.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.adhdaily.R
import com.example.adhdaily.model.entity.Task


class NotificationReceiver : BroadcastReceiver() {
    val REMINDER_CHANNEL_ID: String = "com.example.adhdaily.reminders"
    val GROUP_KEY_REMINDERS: String = "com.example.adhdaily.reminders"
     fun onReceive(context: Context, intent: Intent, task: Task, reminderId: Long) {
        val taskTitle = task.Title
        val taskStartTime = task.StartTime
        val taskIconResId =  intent.getIntExtra("taskIconResId", R.drawable.app_icon_bg)

        val notificationManager = NotificationManagerCompat.from(context)

        // Crear la notificación
        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(taskIconResId)
            .setContentTitle(taskTitle)
            .setContentText(taskStartTime)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(GROUP_KEY_REMINDERS)

        // Mostrar la notificación
         if (ActivityCompat.checkSelfPermission(
                 context,
                 Manifest.permission.POST_NOTIFICATIONS
             ) != PackageManager.PERMISSION_GRANTED
         ) {
             // TODO: Consider calling
             //    ActivityCompat#requestPermissions
             // here to request the missing permissions, and then overriding
             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
             //                                          int[] grantResults)
             // to handle the case where the user grants the permission. See the documentation
             // for ActivityCompat#requestPermissions for more details.
             return
         }
         notificationManager.notify(reminderId.toInt(), builder.build())
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Extraer los datos de la tarea del Intent
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskStartTime = intent.getStringExtra("taskStartTime")
        val taskIconResId = intent.getIntExtra("taskIconResId", R.drawable.app_icon_bg)

        // Crear una notificación
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val notificationChannelId = "com.example.adhdaily.reminders" // El mismo ID de canal que usaste en MainActivity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "ADHDaily Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Calendario para gestionar tareas"
                enableLights(true)
                lightColor = Color.BLUE
                // Puedes configurar otras opciones de canal aquí
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(taskIconResId)
            .setContentTitle("titulo")
            .setContentText("Hora de inicio: ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Aquí puedes agregar otras configuraciones de notificación según tus necesidades

        // Mostrar la notificación
        val notificationId = System.currentTimeMillis().toInt() // ID único para la notificación
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
