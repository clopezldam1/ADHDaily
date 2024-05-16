package com.example.adhdaily.utils

import android.content.Context
import android.content.res.Resources
import android.net.LocalSocketAddress
import android.util.Log
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.time.TimedValue

class ReminderHelper(private val context: Context) {

    /**
     * Obtenemos el LocalDateTime del inicio de la tarea,
     * restamos el tiempo del recordatorio para obtener
     * el LocalDateTime del recordatorio y lo actualizamos
     * en la base de datos con ese valor
     */
    fun setReminderDateTime(timeValue: Long, timeUnitId: Long, selectedTask: Task): LocalDateTime {
        val startDateTask: LocalDate = LocalDate.parse(selectedTask.StartDate)
        var startTimeTask: LocalTime = MainActivity().defaultStartTimeOfTask //default startTime of tasks
        if (!selectedTask.StartTime.equals("null")) {
            startTimeTask = LocalTime.parse(selectedTask.StartTime)
        }
        val startDateTimeTask = LocalDateTime.of(startDateTask, startTimeTask)

        val reminderDateTime: LocalDateTime = when (timeUnitId) {
            0L -> startDateTimeTask.minusMinutes(timeValue)
            1L -> startDateTimeTask.minusHours(timeValue)
            2L -> startDateTimeTask.minusDays(timeValue)
            3L -> startDateTimeTask.minusWeeks(timeValue)
            4L -> startDateTimeTask.minusMonths(timeValue)
            else -> startDateTimeTask //si no se puede calcular su hora, se le pone la de inicio de la tarea
        }

        return reminderDateTime
    }

    /**
     * Crear notificación al crear un recordatorio
     */
    fun createReminderNotif( selectedTask: Task, reminderId: Long, reminderDateTime: LocalDateTime,){
        val notificationHelper = NotificationHelper(context)
        notificationHelper.scheduleNotification(selectedTask, reminderId, reminderDateTime)
    }

    /**
     * Actualizar notificacion al actualizar recordatorio
     */
    fun updateReminderNotif( selectedTask: Task, reminderId: Long, oldReminderDateTime: LocalDateTime, newReminderDateTime: LocalDateTime){
        val notificationHelper = NotificationHelper(context)
        notificationHelper.updateNotification(selectedTask, reminderId, oldReminderDateTime, newReminderDateTime)
    }

}