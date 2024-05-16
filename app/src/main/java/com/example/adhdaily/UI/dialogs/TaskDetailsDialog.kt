package com.example.adhdaily.UI.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.UI.recyclers.RemindersTaskRecycler
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Task
import com.example.adhdaily.utils.ColorTagHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class TaskDetailsDialog(context: Context, private val taskId: Long, private val listener: DialogCloseListener?)
    : Dialog(context, R.style.CustomDialogTheme1), ConfirmDeleteTaskDialog.DialogCloseListener {

    //Listener para recargar recycler dayView despues de cerrar este dialog
    interface DialogCloseListener {
        fun onDialogClosed()
    }

    //COMPONENTES DEL FRAGMENT:
    private var layoutSelectColor: LinearLayout
    private var btnEditTask: ImageButton
    private var btnDeleteTask: ImageButton
    private var btnGoBack: ImageButton
    private var touchTargetBtnGoBack: CardView
    private var touchTargetBtnDeleteTask: CardView
    private var touchTargetBtnEditTask: CardView
    private var btnAddReminder: LinearLayout

    var imgvwColorTagIcon: ImageView
    var txtColorTagName: TextView
    var txtStartDate: TextView
    var txtStartTime: TextView
    var checkAllDay: CheckBox
    var txtDesc: TextView
    var txtTitle: TextView
    var recyclerReminders: RecyclerView

    //VARIABLES DEL DIALOG:
    var titulo: String = ""
    var descripcion: String? = null
    var startDate: LocalDate = MainActivity().selectedDate
    var startTime: LocalTime? = null
    var endDate: String? = null
    var endTime: String? = null
    var completed: Boolean = false
    var colorTagId: Long = 1
    var reminderList: List<Reminder> = emptyList()

    var task: Task //tarea a visualizar o editar


    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_task_details)

        //inicializamos la task a una vacía hasta que se carguen los datos de la task seleccionada
        task = Task(taskId, titulo, descripcion, startDate.toString(), startTime.toString(), "", "", completed, colorTagId)

        //Configurar binding elementos dialog
        layoutSelectColor = findViewById(R.id.layout_openColorTagDialog_taskDetails)
        layoutSelectColor.setOnClickListener {
            openSelectColorTagDialog()
        }

        imgvwColorTagIcon = findViewById(R.id.imgvw_colorTagIcon_taskDetails)
        txtColorTagName = findViewById(R.id.txt_colorTagName_taskDetails)

        txtStartDate = findViewById(R.id.txt_startDate_taskDetails)
        txtStartDate.setOnClickListener {
            openDatePickerDialogStartDate()
        }

        txtStartTime = findViewById(R.id.txt_startTime_taskDetails)
        txtStartTime.setOnClickListener {
            openHourPickerDialogStartTime()
        }

        checkAllDay = findViewById(R.id.checkAllDay_taskDetails)
        checkAllDay.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked){
                txtStartTime.isEnabled = false
                txtStartTime.alpha = 0.3f
            }else{
                txtStartTime.isEnabled = true
                txtStartTime.alpha = 1f
            }
        }

        txtDesc = findViewById(R.id.txt_desc_taskDetails)
        txtTitle = findViewById(R.id.txt_title_taskDetails)

        btnEditTask = findViewById(R.id.btn_editTask)
        btnEditTask.setOnClickListener {
            //Title is a required field for task, can't create if empty
            if(!txtTitle.text.isNullOrBlank()) {
                //startTime is required field when checkAllDay is NOT checked
                if(!checkAllDay.isChecked && txtStartTime.text.isNullOrBlank()){
                    Toast.makeText(context.applicationContext, R.string.toast_startTimeRequired, Toast.LENGTH_SHORT).show()
                } else {
                    editTask()
                }
            } else {
                Toast.makeText(context.applicationContext, R.string.toast_titleRequired, Toast.LENGTH_SHORT).show()
            }
        }
        touchTargetBtnEditTask = findViewById(R.id.touchTarget_imgbtn_editTask)
        touchTargetBtnEditTask.setOnClickListener{
            btnEditTask.callOnClick()
        }

        btnDeleteTask = findViewById(R.id.btn_deleteTask)
        btnDeleteTask.setOnClickListener {
            deleteTask()
        }
        touchTargetBtnDeleteTask = findViewById(R.id.touchTarget_imgbtn_deleteTask)
        touchTargetBtnDeleteTask.setOnClickListener{
            btnDeleteTask.callOnClick()
        }

        btnGoBack = findViewById(R.id.btn_goBack)
        btnGoBack.setOnClickListener {
            closeDialog()
        }
        touchTargetBtnGoBack = findViewById(R.id.touchTarget_imgbtn_goBack)
        touchTargetBtnGoBack.setOnClickListener{
            btnGoBack.callOnClick()
        }

        recyclerReminders = findViewById(R.id.recycler_remindersInTask_taskDetails)
        recyclerReminders.layoutManager = LinearLayoutManager(context)

        btnAddReminder = findViewById(R.id.btn_addReminder_taskDetails)
        btnAddReminder.setOnClickListener{
            openReminderPickerDialog(null)
        }

        //Rellenar formulario con datos de la tarea
        loadTaskDetails()

    }

    /**
     * Cerrar el diálogo
     */
    private fun closeDialog() {
        this.dismiss()
    }

    /**
     * Llama al método del listener cuando se cierra el dialog
     */
    override fun dismiss() {
        super.dismiss()
        listener!!.onDialogClosed()
    }

    /**
     * Cuando se cierra el dialogo de confirmDeleteTask, este también se cierra
     */
    override fun onDialogClosed(confirmDelete: Boolean) {
        if(confirmDelete){
            closeDialog()
        }
    }

    /**
     * Poner el colorTag que estaba seleccionado originalmente en la tarea
     */
    private fun setTaskColorTag() {
        val helper = ColorTagHelper()
        helper.setTextToTextViewFromColorTagId(this.colorTagId , context, this.txtColorTagName)
        helper.setColorToImageViewFromColorTagId(this.colorTagId , context, this.imgvwColorTagIcon)
    }

    /**
     * Abre el diálogo que establece el colorTag de la tarea a editar
     */
    private fun openSelectColorTagDialog() {
        val dialog = ColorSelectDialog(context,null, this)
        dialog.show()
    }

    /**
     * Abre el diálogo que establece el startDate de la tarea a editar
     */
    private fun openDatePickerDialogStartDate() {
        //Obtener la fecha ORIGINAL DE LA TAREA para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()
        calendar.set(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val formattedDate = MainActivity().simpleDateFormat.format(selectedDate.time)

                // Establecer la fecha formateada en el EditText
                startDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
                txtStartDate.setText(formattedDate)

                //Modificamos selectedDate en la activity (variable global)
                MainActivity().selectedDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog.show()
    }

    /**
     * Abre el diálogo que establece el startTime de la tarea a editar
     */
    private fun openHourPickerDialogStartTime() {
        //Obtener la hora ESTABLECIDA ORIGNALMENTE EN LA TAREA para preseleccionarla
        var hour = 0
        var minute = 0
        if(startTime == null){
            val cal = Calendar.getInstance()
            hour = cal.get(Calendar.HOUR_OF_DAY)
            minute = cal.get(Calendar.MINUTE)
        }else{
            hour = startTime!!.hour
            minute = startTime!!.minute
        }

        //crea TimePickerDialog para seleccionar la hora
        val timePickerDialog = TimePickerDialog(
            context,
            R.style.CustomTimePickerDialogTheme,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                // Actualizar el TextView con la hora seleccionada
                startTime = LocalTime.parse(String.format("%02d:%02d", selectedHour, selectedMinute))
                txtStartTime.text = startTime.toString()
            },
            hour,
            minute,
            MainActivity().time24hFormat
        )
        timePickerDialog.show()
    }

    private fun editTask(){
        val localDB = LocalDatabase.getInstance(this.context)

        GlobalScope.launch(Dispatchers.IO) {
            titulo = txtTitle.text.toString()
            descripcion = txtDesc.text.toString()

            //TODO: actualizar dateTime de todos los recordatorios de esa task

            try {
                task = Task(taskId, titulo, descripcion, startDate.toString(), startTime.toString(), endDate ,endTime, completed, colorTagId)
                localDB.taskDao().update(task)
            }catch (ex:Exception){
                Log.e("CATCH", "editTask: " + ex.message)
            }
        }
        Toast.makeText(context.applicationContext, R.string.toast_taskEditedSuccess, Toast.LENGTH_SHORT).show()
    }

    /**
     * Para eliminar una tarea, primero aceptar diálogo
     * de confirmación, luego eliminar de BD
     */
    private fun deleteTask() {
        task = Task(taskId, titulo, descripcion, startDate.toString(), startTime.toString(), endDate ,endTime, completed, colorTagId)
        val dialog = ConfirmDeleteTaskDialog(context,task,this)
        dialog.show()

        dialog.setOnDismissListener {
            //if user confirmDelete = true, task is deleted from DB and both dialogs close (if delete was cancelled nothing happens)
            if(dialog.confirmDelete){
                dialog.dismiss()
            }
        }
    }

    /**
     * Hace la consulta de la tarea para poder inicializar las variables y los campos
     */
    private fun getSelectedTask(callback: (Task?) -> Unit) {
        val localDB = LocalDatabase.getInstance(this.context)
        //abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //consultar tarea
                task = localDB.taskDao().selectTaskById(taskId)
                Log.i("detail", "getSelectedTask: " + task.toString())
                callback(task)
            } catch (ex: Exception) {
                Log.i("CATCH", "getSelectedTask: " + ex.message)
                callback(null)
            }
        }
    }

    /**
     * Cargar los detalles originales de la tarea para poder visualizar y editarlos
     */
    private fun loadTaskDetails() {
        //llamamos a getSelectedTask y no hacemos nada hasta que no termine de realizar la consulta y meta el valor en la variable global
        getSelectedTask { task ->
            task?.let {
                Log.i("detail", "loadTaskDetails: " + task.toString())

                //almacenar datos de la tarea en variables globales
                titulo = task.Title
                descripcion = task.Desc
                startDate = LocalDate.parse(task.StartDate)
                if (task.StartTime != "null") {
                    startTime = LocalTime.parse(task.StartTime)
                }
                endDate = task.EndDate
                endTime = task.EndTime
                completed = task.Completed
                colorTagId = task.ColorTag_FK
            }

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                try {
                    //rellenar form con datos de la tarea
                    txtTitle.text = titulo
                    txtDesc.text = descripcion
                    txtStartDate.text = startDate.toString()
                    Log.i("detail", "loadTaskDetails: titulo " + txtStartDate.text)

                    if (task?.StartTime == "null") {
                        checkAllDay.isChecked = true
                        txtStartTime.isEnabled = false
                    } else {
                        txtStartTime.text = startTime.toString()
                        txtStartTime.isEnabled = true
                        checkAllDay.isChecked = false
                    }

                    setTaskColorTag()
                    loadRecyclerReminders()
                } catch (ex: Exception) {
                    Log.e("CATCH", "loadTaskDetails: " + ex.message)
                }
            }

        }
    }

    /**
     * Al darle al botón de añadir recordatorio, abrir el diálogo para
     * seleccionar un recordatorio y, cuando ese diálogo se cierre, recargar
     * el recycler de recordatorios.
     */
    private fun openReminderPickerDialog(selectedReminderId: Long?){
        val reminderPickerDialog = ReminderPickerDialog(context, selectedReminderId, task)
        reminderPickerDialog.show()

        reminderPickerDialog.setOnDismissListener {
            loadRecyclerReminders()
        }
    }

    /**
     * Método que realiza la carga del RecyclerView (consulta en un hilo a parte)
     */
     fun loadRecyclerReminders() {
         val taskDetailsDialog: TaskDetailsDialog = this

        GlobalScope.launch(Dispatchers.IO) {
            reminderList = getReminderListTask()
            withContext(Dispatchers.Main) {
                if (!reminderList.isEmpty()) {
                    recyclerReminders.adapter = RemindersTaskRecycler(reminderList, taskDetailsDialog, task)
                } else {
                    recyclerReminders.adapter = null
                }
            }
        }
    }

    /**
     * Realizar consulta para obtener la lista de recordatorios de la tarea (ejecutar en hilo a parte)
     */
    private suspend fun getReminderListTask(): List<Reminder> {
        return withContext(Dispatchers.IO) {
            val localDatabase: LocalDatabase = LocalDatabase.getInstance(context)
            localDatabase.reminderDao().selectRemindersOnTask(taskId)
        }
    }

}