package com.example.adhdaily.UI.fragments.newTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.UI.dialogs.ColorSelectDialog
import com.example.adhdaily.databinding.FragmentNewTaskBinding
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private lateinit var navController: NavController

    //COMPONENTES DEL FRAGMENT:
    private lateinit var layoutSelectColor: LinearLayout
    private lateinit var btnCreateTask: Button
    private lateinit var btnAddReminder: LinearLayout

    lateinit var imgvwColorTagIcon: ImageView
    lateinit var txtColorTagName: TextView
    lateinit var txtStartDate: TextView
    lateinit var txtStartTime: TextView
    lateinit var checkAllDay: CheckBox
    lateinit var txtDesc: TextView
    lateinit var txtTitle: TextView
    lateinit var recyclerReminders: RecyclerView

    //VARIABLES DEL FRAGMENT:
    var newTaskId: Long = 0
    var titulo: String = ""
    var descripcion: String? = null
    var startDate: LocalDate = MainActivity().selectedDate
    var startTime: LocalTime? = null
    var endDate: String? = null
    var endTime: String? = null
    var completed: Boolean = false
    var colorTagId: Long = 1

    lateinit var newTask: Task //la nueva tarea a crear

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //INFLAR VISTA:
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //INICIALIZAR COMPONENTES DE LA VISTA:
        layoutSelectColor = binding.layoutOpenColorTagDialog
        layoutSelectColor.setOnClickListener {
            openSelectColorTagDialog()
        }

        imgvwColorTagIcon = binding.imgvwColorTagIcon
        txtColorTagName = binding.txtColorTagName

        txtStartDate = binding.txtStartDate
        txtStartDate.setOnClickListener {
            openDatePickerDialogStartDate()
        }

        txtStartTime = binding.txtStartTime
        txtStartTime.setOnClickListener {
            openHourPickerDialogStartTime()
        }

        checkAllDay = binding.checkAllDay
        checkAllDay.setOnCheckedChangeListener {_, isChecked ->
           if(isChecked){
               txtStartTime.isEnabled = false
               txtStartTime.alpha = 0.3f
           }else{
               txtStartTime.isEnabled = true
               txtStartTime.alpha = 1f
           }
        }

        txtDesc = binding.txtDesc
        txtTitle = binding.txtTitle

        btnCreateTask = binding.btnAddNewTask
        btnCreateTask.setOnClickListener {
            //Title is a required field for newTask, can't create if empty
            if(!txtTitle.text.isNullOrBlank()) {
                //startTime is required field when checkAllDay is NOT checked
                if(!checkAllDay.isChecked && txtStartTime.text.isNullOrBlank()){
                    Toast.makeText(requireContext().applicationContext, R.string.toast_startTimeRequired, Toast.LENGTH_SHORT).show()
                } else {
                    createNewTask()
                }
            } else {
                Toast.makeText(requireContext().applicationContext, R.string.toast_titleRequired, Toast.LENGTH_SHORT).show()
            }
        }

        recyclerReminders = binding.recyclerRemindersInTask
        recyclerReminders.layoutManager = LinearLayoutManager(context)

        btnAddReminder = binding.btnAddReminder
        btnAddReminder.setOnClickListener{
            //openReminderPickerDialog(null)
        }


        return root
    }

    //Método para cuando se ha creado la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //deshabilitar el boton selectDate del toolbar cuando se cierra este fragment
        /*TODO: disable the selectDate button from toolbar.
        //This code rn explodes bc its not able to inflate the mainActivity properly, u gotta find a way of getting the button that actually grabs the new instance of the button each time the activity is recreated

        (activity as MainActivity).btnSelectDate.isEnabled = false
        (activity as MainActivity).btnSelectDate.alpha = 0.5f
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        //volver a habilitar el boton selectDate del toolbar cuando se cierra este fragment
        /*TODO: disable the selectDate button from toolbar.
        //This code rn explodes bc its not able to inflate the mainActivity properly, u gotta find a way of getting the button that actually grabs the new instance of the button each time the activity is recreated

        (activity as MainActivity).btnSelectDate.isEnabled = true
        (activity as MainActivity).btnSelectDate.alpha = 1f
         */

        cleanForm()
    }

    /**
     * Cada vez que cambiamos de fragmento y volvemos a este,
     * se nos limpia el formulario
     */
    override fun onResume() {
        super.onResume()
        cleanForm()
        getNewTaskId()
    }

    private fun getNewTaskId() {
        val localDB = LocalDatabase.getInstance(this.requireContext())
        GlobalScope.launch(Dispatchers.IO) {
            newTaskId = localDB.taskDao().selectLastTaskId() + 1 //consultar last ID +1
        }
    }

    private fun createNewTask(){
        val localDB = LocalDatabase.getInstance(this.requireContext())

        startDate = LocalDate.parse(txtStartDate.text.toString(),MainActivity().dateTimeFormatter)
        MainActivity().selectedDate = startDate

        //Abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            titulo = txtTitle.text.toString()
            descripcion = txtDesc.text.toString()
            //startDate = LocalDate.parse(txtStartDate.text.toString(),MainActivity().dateTimeFormatter)

            try {
                newTask = Task(newTaskId, titulo, descripcion, startDate.toString(), startTime.toString(), endDate ,endTime, completed, colorTagId)

                localDB.taskDao().insert(newTask)
            }catch (ex:Exception){
                Log.e("CATCH", "createNewTask: " + ex.message)
            }

        }
        //después de crearla, limpiamos el formulario
        cleanForm()

        Toast.makeText(requireContext().applicationContext, R.string.toast_taskCreatedSuccess, Toast.LENGTH_SHORT).show()

        navController = (activity as MainActivity).navController
        //TODO: redirigir a algún lado después de crear esa tarea, no se a donde lol igual al month view?
        navController.navigate(R.id.navigation_dayView) //cambiar a month view cuando esté hecho
    }

    /**
     * Abre el diálogo que establece el colorTag de la nueva tarea
     */
    private fun openSelectColorTagDialog() {
        val dialog = ColorSelectDialog(requireContext(),this, null)
        dialog.show()
    }

    /**
     * Abre el diálogo que establece el startDate de la nueva tarea
     */
    private fun openDatePickerDialogStartDate() {
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val formattedDate = (activity as MainActivity).simpleDateFormat.format(selectedDate.time)
                startDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
                txtStartDate.setText(formattedDate)

                MainActivity().selectedDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        datePickerDialog.show()
    }

    /**
     * Abre el diálogo que establece el startTime de la nueva tarea
     */
    private fun openHourPickerDialogStartTime() {
        // Obtener la hora actual del sistema para preseleccionarla
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        //crear TimePickerDialog para seleccionar la hora
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialogTheme,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                startTime = LocalTime.parse(String.format("%02d:%02d", selectedHour, selectedMinute))
                txtStartTime.text = startTime.toString()
            },
            hour,
            minute,
            MainActivity().time24hFormat
        )
        timePickerDialog.show()
    }

    /*//TODO: FUNCIONALIDAD DE CREAR RECORDATORIOS A LA VEZ QUE CREAS LA TAREA DEJADA PARA UPDATE FUTURA
    /**
     * Al darle al botón de añadir recordatorio, abrir el diálogo para
     * seleccionar un recordatorio y, cuando ese diálogo se cierre, recargar
     * el recycler de recordatorios.
     */
    private fun openReminderPickerDialog(selectedReminderId: Long?){
        //inicializamos la nueva tarea con datos provisionales (para poder meterle recordatorios)
        getNewTaskId()
        newTask = Task(newTaskId, titulo, descripcion, startDate.toString(), startTime.toString(), endDate ,endTime, completed, colorTagId)
        Log.i("reminder", "openReminderPickerDialog: " + newTaskId)
        Log.i("reminder", "openReminderPickerDialog: " + newTask.TaskId)
        val reminderPickerDialog = ReminderPickerDialog(requireContext(),null, newTask)
        reminderPickerDialog.show()

        reminderPickerDialog.setOnDismissListener {
            loadRecyclerReminders()
        }

    }

    /**
     * Método que realiza la carga del RecyclerView (consulta en un hilo a parte)
     */
    private fun loadRecyclerReminders() {
        // Obtener la lista de tareas en un hilo de fondo
        GlobalScope.launch(Dispatchers.IO) {
            reminderList = getReminderListTask()
            withContext(Dispatchers.Main) {
                if (!reminderList.isEmpty()) {
                    recyclerReminders.adapter = RemindersTaskRecycler(reminderList)
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
            val localDatabase: LocalDatabase = LocalDatabase.getInstance(requireContext())
            localDatabase.reminderDao().selectRemindersOnTask(newTaskId)
        }
    }
     */

    /**
     * Vuelve a poner el formulario en blanco
     */
    private fun cleanForm(){
        txtDesc.setText("")
        txtTitle.setText("")
        txtStartDate.setText((activity as MainActivity).selectedDate.format(MainActivity().dateTimeFormatter).toString())
        txtStartTime.text = null
        imgvwColorTagIcon.setColorFilter(Color.parseColor("#A5A7AF")) //@colors/gray2
        txtColorTagName.setText(R.string.lbl_colorTagNone)
        checkAllDay.isChecked = true
    }

}