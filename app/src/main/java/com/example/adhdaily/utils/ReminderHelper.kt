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
     * Obtiene el texto correspondiente a un timeUnitId (según su posición en el array)
     *
     * 0=min,1=hour,2=day,3=week,4=month
     */
    fun getTimeUnitById(timeUnitId: Long): String{
        val timeUnitValuesArray = context.resources.getStringArray(R.array.time_unit_values_reminders)
        return timeUnitValuesArray[timeUnitId.toInt()]
    }

    /**
     * Obtiene el array de timeUnits para cargarlo en el selector
     *
     * 0=min,1=hour,2=day,3=week,4=month
     */
    fun getTimeUnitArray(): Array<String> {
        return context.resources.getStringArray(R.array.time_unit_values_reminders)
    }

    /**
     * Obtenemos el LocalDateTime del inicio de la tarea,
     * restamos el tiempo del recordatorio para obtener
     * el LocalDateTime del recordatorio y lo actualizamos
     * en la base de datos con ese valor
     */
    fun setReminderDateTime(timeValue: Long, timeUnitId: Long, selectedTask: Task): LocalDateTime {
        val startDateTask: LocalDate = LocalDate.parse(selectedTask.StartDate)
        var startTimeTask: LocalTime = LocalTime.parse(selectedTask.StartTime)
        if (selectedTask.StartTime.equals("null")) {
            startTimeTask = MainActivity().defaultStartTimeOfTask //default startTime of tasks
        }
        val startDateTimeTask = LocalDateTime.of(startDateTask, startTimeTask)
        Log.i("reminder", "setReminderDateTime: START TASK:  " + startDateTimeTask)

        val reminderDateTime: LocalDateTime = when (timeUnitId) {
            0L -> startDateTimeTask.minusMinutes(timeValue)
            1L -> startDateTimeTask.minusHours(timeValue)
            2L -> startDateTimeTask.minusDays(timeValue)
            3L -> startDateTimeTask.minusWeeks(timeValue)
            4L -> startDateTimeTask.minusMonths(timeValue)
            else -> startDateTimeTask //si no se puede calcular su hora, se le pone la de inicio de la tarea
        }

        Log.i("reminder", "setReminderDateTime: timeUnit: " + timeUnitId)
        Log.i("reminder", "setReminderDateTime: timeValue: " + timeValue)
        Log.i("reminder", "setReminderDateTime: REMINDER: " + reminderDateTime)
        /*
        val localDB = LocalDatabase.getInstance(this.context)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i("reminder", "setReminderDateTime: UPDATE " + reminderDateTime)
                localDB.reminderDao().updateDateTimeReminder(reminderId, reminderDateTime.toString())
            }catch (ex:Exception){
                Log.e("CATCH", "setReminderDateTime: " + ex.message)
            }
        }
         */


        val notificationHelper = NotificationHelper(context)
        notificationHelper.setReminderNotification(reminderDateTime)

        return reminderDateTime
    }


}