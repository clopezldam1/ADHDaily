package com.example.adhdaily.UI.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.adhdaily.MainActivity
import com.example.adhdaily.R
import com.example.adhdaily.databinding.DialogSelectDateBinding
import java.text.DateFormat

class SelectDateDialog(context: Context) : Dialog(context,R.style.CustomDialogTheme) {

    private lateinit var binding: DialogSelectDateBinding
    lateinit var txtSelectDate: TextView
    lateinit var btnGotoDate: Button

    private val dateFormatter = MainActivity().dateFormatter //SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_select_date)

        //Configurar binding elementos dialog
        txtSelectDate = findViewById<TextView>(R.id.txt_inputSelectedDate)
        txtSelectDate.setOnClickListener {
            openDatePickerDialog()
        }

        btnGotoDate = findViewById<Button>(R.id.btn_gotoDate)
        btnGotoDate.setOnClickListener {
            // Lógica para el clic en el botón
            goToDate()
        }
    }

    private fun goToDate() {
        Toast.makeText(context, "gotoDate", Toast.LENGTH_SHORT).show()

        //cerrar el dialog
        this.dismiss()

        //TODO: te lleva al DayView de la fecha seleccionada
    }

    private fun openDatePickerDialog() {
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val formattedDate = this.dateFormatter.format(selectedDate.time)

                // Establecer la fecha formateada en el EditText
                txtSelectDate.setText(formattedDate)

                //Modificamos selectedDate en la activity (variable global)
                MainActivity().selectedDate = selectedDate.time
                //Log.i("PATATA", "openDatePickerDialog- SELECTED DATE: " +  MainActivity().selectedDate) //Sun Apr 21 11:21:30 GMT+02:00 2024
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }


}