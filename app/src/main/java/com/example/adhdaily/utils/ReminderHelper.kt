package com.example.adhdaily.utils

import android.content.Context
import android.content.res.Resources
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.model.entity.Task
import java.time.LocalDate
import java.time.LocalTime
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

    fun setDateTime(timeValue: Long, timeUnitId: Long, selectedTask: Task) {
        val startDateTask: LocalDate = LocalDate.parse(selectedTask.StartDate)
        var startTimeTask: LocalTime = MainActivity().defaultStartTimeOfTask //default startTime of tasks
        if (selectedTask.StartTime.equals("null")) {
            startTimeTask = LocalTime.parse(selectedTask.StartTime)
        }


    }

}