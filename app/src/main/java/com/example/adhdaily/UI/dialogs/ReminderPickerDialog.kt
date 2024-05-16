package com.example.adhdaily.UI.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.adhdaily.R
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Task
import com.example.adhdaily.utils.ReminderHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ReminderPickerDialog(context: Context, private val selectedReminderId : Long?, private val selectedTask: Task) : Dialog(context, R.style.CustomDialogTheme1){

    //VARIABLES DE LA VISTA
    var btnConfirmar: Button
    var btnCancelar: Button
    var txtTimeValue: EditText
    var spinnerTimeUnitSelector: Spinner

    //VARIABLES DEL FRAGMENT:
    var remId: Long = 0 //en el init, segun editemos o creemos, le damos un id u otro
    var remText: String = ""
    var remDateTime: LocalDateTime = LocalDateTime.now()
    var timeValue: Long = 5
    var timeUnitId: Long = 0 //id de la lista del selector
    var taskFk: Long = selectedTask.TaskId

    init{
        //Darle el layout al dialog
        setContentView(R.layout.dialog_reminder_picker)

        btnConfirmar = findViewById(R.id.btn_confirmReminder)
        btnConfirmar.setOnClickListener {
            saveReminder() //hace update or insert
        }

        btnCancelar = findViewById(R.id.btn_cancelReminder)
        btnCancelar.setOnClickListener {
            this.dismiss() //cerrar dialog y no hacer nada
        }

        txtTimeValue = findViewById(R.id.txt_timeValueSelector)
        txtTimeValue.doOnTextChanged { text, _, _, _ ->
            if (!txtTimeValue.text.isNullOrEmpty()) {
                timeValue = txtTimeValue.text.toString().toLong()

            }
        }

        //inicializar selector de timeUnit y ponerle el adapter con el array
        spinnerTimeUnitSelector = findViewById(R.id.spinner_timeUnitSelector)
        val adapterSelectTimeUnit = ArrayAdapter.createFromResource(
            context,
            R.array.time_unit_values_reminders,
            android.R.layout.simple_spinner_item
        )
        adapterSelectTimeUnit.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinnerTimeUnitSelector.adapter = adapterSelectTimeUnit

        //distinguir entre recordatorio EDIT o CREATE
        if (selectedReminderId != null) {
            //editar reminder existente
            loadReminderDetails()
        } else {
            //crear nuevo reminder
            setDefaultDataInFields()
        }

    }

    /**
     * Según el recordatorio exista o no,
     * se hará UPDATE o INSERTs
     */
    private fun saveReminder() {
        buildReminderText()

        //timeValue = txtTimeValue.text.toString().toLong()
        //timeUnitId = spinnerTimeUnitSelector.selectedItemId
        buildReminderDateTime()

        if (selectedReminderId != null) {
            //editar reminder existente
            updateReminder()
        } else {
            //crear nuevo reminder
            createReminder()
        }

        this.dismiss() //cerrar después de guardar

    }

    /**
     * Una vez seleccionada la configuración del reminder,
     * construimos el texto del reminder según lo seleccinado
     */
    private fun buildReminderText() {
        val timeValue = txtTimeValue.text.toString()
        val timeUnitText = spinnerTimeUnitSelector.selectedItem.toString()
        val strReminderText_before = context.resources.getString(R.string.txt_reminderFormBefore)
        remText = "${timeValue} ${timeUnitText} ${strReminderText_before}"
    }

    private fun buildReminderDateTime(){
        timeValue = txtTimeValue.text.toString().toLong()
        timeUnitId = spinnerTimeUnitSelector.selectedItemId

        //Actualizamos la hora del recordatorio cuando terminamos de editar tarea
        val reminderHelper = ReminderHelper(context)
        remDateTime = reminderHelper.setReminderDateTime(timeValue, timeUnitId, selectedTask)
        Log.i("reminder", "saveReminder: " +  remDateTime.toString())
    }

    /**
     * Rellenar campos con datos por defecto
     * cuando vas a CREAR un recordatorio
     *
     * Por defecto, te sugiere crear un recordatorio
     * 5 minutos antes del timeStart de la tarea
     */
    private fun setDefaultDataInFields() {
        txtTimeValue.setText("5")
        spinnerTimeUnitSelector.setSelection(0)
    }

    /**
     * Rellenar campos con datos del reminder
     * cuando vas a EDITAR un recordatorio
     */
    private fun loadReminderDetails() {
        Log.i("reminder", "loadReminderDetails: 1")
        //llamamos a getSelectedTask y no hacemos nada hasta que no termine de realizar la consulta y meta el valor en la variable global
        getSelectedReminder { reminder ->
            reminder?.let {
                Log.i("reminder", "loadReminderDetails: 2")
                //almacenar datos del reminder en variables globales
                remText = reminder.Text
                remDateTime = LocalDateTime.parse(reminder.DateTimeReminder)
                timeValue = reminder.TimeValue
                timeUnitId = reminder.TimeUnitId
                taskFk = reminder.TaskId_FK

                Log.i("reminder", "loadReminderDetails: reminder.TimeValue:" + reminder.TimeValue)
                Log.i("reminder", "loadReminderDetails: reminderTimeValue:" + timeValue)

                //rellenar form con datos del recordatorio
                txtTimeValue.setText(timeValue.toString())
                spinnerTimeUnitSelector.setSelection(timeUnitId.toInt())
            }
        }
    }

    /**
     * Hace la consulta de la tarea para poder inicializar las variables y los campos
     */
    private fun getSelectedReminder(callback: (Reminder?) -> Unit) {
        val localDB = LocalDatabase.getInstance(this.context)
        //abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val reminder = localDB.reminderDao().selectReminderById(selectedReminderId!!)
                Log.i("reminder", "getSelectedReminder:  CONSULTA" +  reminder.ReminderId)
                callback(reminder)
            } catch (ex: Exception) {
                Log.e("CATCH", "getSelectedReminder: " + ex.message)
                callback(null)
            }
        }
    }

    /**
     * Inserta el recordatorio en la base de datos
     */
    private fun createReminder() {
        val localDB = LocalDatabase.getInstance(this.context)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i("reminder", "createReminder: taskFK: " + taskFk)
                localDB.reminderDao().insertReminderIntoTask(remText, remDateTime.toString(), timeValue, timeUnitId, taskFk)
            } catch (ex: Exception) {
                Log.i("CATCH", "createReminder: " + ex.message)
            }
        }
        Toast.makeText(context.applicationContext, R.string.toast_reminderCreatedSuccess, Toast.LENGTH_SHORT).show()
    }

    /**
     * Actualiza el recordatorio en la base de datos
     */
    private fun updateReminder() {
        val localDB = LocalDatabase.getInstance(this.context)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val reminder = Reminder(selectedReminderId!!, remText, remDateTime.toString(), timeValue, timeUnitId, taskFk)
                localDB.reminderDao().update(reminder)
                Toast.makeText(context.applicationContext, R.string.toast_reminderEditedSuccess, Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Log.i("CATCH", "updateReminder: " + ex.message)
            }
        }
    }

    /**
     * Obtiene el texto de un TimeValueText
     * a partir de un TimeValueId
     */
    private fun getTimeUnitText(timeValueId: Long): String{
        val reminderHelper = ReminderHelper(context)
        return reminderHelper.getTimeUnitById(timeValueId)
    }


}