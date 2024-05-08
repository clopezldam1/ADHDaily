package com.example.adhdaily.UI.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.R
import com.example.adhdaily.databinding.DialogSelectDateBinding
import com.example.adhdaily.utils.DateTimePickerDialogs
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SelectDateDialog(context: Context) : Dialog(context,R.style.CustomDialogTheme1) {

    private lateinit var binding: DialogSelectDateBinding
    lateinit var txtSelectDate: TextView
    lateinit var btnGotoDate: Button

    private val dateFormatter = MainActivity().simpleDateFormat //SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_select_date)

        //Configurar binding elementos dialog
        txtSelectDate = findViewById<TextView>(R.id.txt_inputSelectedDate)
        txtSelectDate.setOnClickListener {
            openDatePickerDialog()

            //TODO: lograr usar estos dialogs pequeños en una clase de utils para reducir codigo repetido
            /*
            val datePicker = DateTimePickerDialogs()
            datePicker.openDatePickerDialog(context)
            txtSelectDate.setText(datePicker.formattedDate)
            */
        }

        btnGotoDate = findViewById<Button>(R.id.btn_gotoDate)
        btnGotoDate.setOnClickListener {
            goToDate()
        }
    }

    private fun goToDate() {
        //cerrar dialog
        this.dismiss()

        //TODO: te lleva al DayView de la fecha seleccionada
        Toast.makeText(context, "gotoDate", Toast.LENGTH_SHORT).show()
    }

    private fun openDatePickerDialog() {
        //obtener la fecha actual para preseleccionarla en DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                //formatear fecha seleccionada en formato deseado
                val formattedDate = this.dateFormatter.format(selectedDate.time)

                //establecer fecha formateada en editText
                txtSelectDate.setText(formattedDate)

                //modificar selectedDate en la activity (variable global)
                val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                MainActivity().selectedDate = LocalDate.parse(formattedDate, pattern)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        datePickerDialog.show()
    }


}