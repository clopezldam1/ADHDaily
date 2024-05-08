package com.example.adhdaily.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.DatePicker
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateTimePickerDialogs {
    //estas variables se quedan vacías bc hay que acceder despues de que
    // se haya cerrado dialog, que es cuando se selcciona la fecha
    var formattedDate: String = ""
    var formattedTime: String = ""

    fun showTimePickerDialog(context: Context) {
        // Obtener la hora actual del sistema
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)


        // Crear un TimePickerDialog para seleccionar la hora
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                // Actualizar el TextView con la hora seleccionada
                this.formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            },
            hour,
            minute,
            true // Indica si el diálogo permite elegir la hora en formato 24 horas
        )

        // Mostrar el diálogo
        timePickerDialog.show()
    }

    fun openDatePickerDialog(context: Context) {
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val dateFormatter = MainActivity().simpleDateFormat
                this.formattedDate = dateFormatter.format(selectedDate.time)

                //Modificamos selectedDate en la activity (variable global)
                val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                MainActivity().selectedDate = LocalDate.parse(formattedDate, pattern)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }
}