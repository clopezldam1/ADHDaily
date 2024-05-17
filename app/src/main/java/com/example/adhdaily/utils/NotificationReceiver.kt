package com.example.adhdaily.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity



class NotificationReceiver : BroadcastReceiver() {
    val REMINDER_CHANNEL_ID: String = "com.example.adhdaily.reminders"
    val GROUP_KEY_REMINDERS: String = "reminders"

    /**
     * Creamos la notificacion:
     * La hora y titulo aparecen arriba y, si tiene descripcion, aparece debajo.
     * Si pulsas la notificación, se abre la app.
     * Si llegan varias, son stackable.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Extraer los datos de la tarea del Intent
        val taskTitle = intent.getStringExtra("taskTitle")
        val taskStartTime = intent.getStringExtra("taskStartTime")
        val taskDesc = intent.getStringExtra("taskDesc")
//        val remId = intent.getStringExtra("remId")


        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        var contentTitle = ""
        if (taskStartTime.equals("null")){
            contentTitle = taskTitle!!
        }else{
            contentTitle = "${taskStartTime} - ${taskTitle}"
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon_bg)
            .setContentTitle(contentTitle)
            .setContentText(taskDesc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(GROUP_KEY_REMINDERS)
            .setGroupSummary(false)
            .setContentIntent(pendingIntent)
//            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            //.addAction(R.drawable.baseline_checklist_24, context.getString(R.string.lbl_openApp), pendingIntent)

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
