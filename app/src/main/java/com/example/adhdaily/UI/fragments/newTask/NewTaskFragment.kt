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
import androidx.navigation.findNavController
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
import java.util.Locale


class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null
    private lateinit var navController: NavController

    //COMPONENTES DEL FRAGMENT:
    private lateinit var layoutSelectColor: LinearLayout
    private lateinit var btnCreateTask: Button

    lateinit var imgvwColorTagIcon: ImageView
    lateinit var txtColorTagName: TextView
    lateinit var txtStartDate: TextView
    lateinit var txtStartTime: TextView
    lateinit var checkAllDay: CheckBox
    lateinit var txtDesc: TextView
    lateinit var txtTitle: TextView

    //VARIABLES DEL FRAGMENT:
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
        navController = (activity as MainActivity).navController

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
                    Toast.makeText(requireContext().applicationContext, R.string.toast_startTime, Toast.LENGTH_SHORT).show()
                } else {
                    createNewTask()
                }
            } else {
                Toast.makeText(requireContext().applicationContext, R.string.toast_titleRequired, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    //Método para cuando se ha creado la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //deshabilitar el boton selectDate del toolbar cuando se cierra este fragment
        /*TODO: disable the selectDate button from toolbar.
        //This code rn explodes bc its not able to inflate the mainActivity properly,
        //u gotta find a way of getting the button that actually grabs the new instance of the
        //button each time the activity is recreated

        (activity as MainActivity).btnSelectDate.isEnabled = false
        (activity as MainActivity).btnSelectDate.alpha = 0.5f
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        //volver a habilitar el boton selectDate del toolbar cuando se cierra este fragment
        /*TODO: disable the selectDate button from toolbar.
        //This code rn explodes bc its not able to inflate the mainActivity properly,
        //u gotta find a way of getting the button that actually grabs the new instance of the
        //button each time the activity is recreated

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
    }

    private fun createNewTask(){
        val localDB = LocalDatabase.getInstance(this.requireContext())

        //Abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("fecha", "addTrialTask:  LocalDate.now()" +  LocalDate.now())
            Log.i("fecha", "addTrialTask:  LocalTime.now()" +  LocalTime.now())
            Log.i("fecha", "createNewTask: " + startDate)

            titulo = txtTitle.text.toString()
            descripcion = txtDesc.text.toString()

            val newtTaskID = localDB.taskDao().selectLastTaskId() + 1 //consultar last ID +1
            try {
                newTask = Task(newtTaskID, titulo, descripcion, startDate.toString(), startTime.toString(), endDate ,endTime, completed, colorTagId)
                localDB.taskDao().insert(newTask)
            }catch (ex:Exception){
                Log.i("CATCH", "addTrialTask: " + ex.message)
            }

        }
        //después de crearla, limpiamos el formulario
        cleanForm()

        Toast.makeText(requireContext().applicationContext, R.string.toast_taskCreatedSuccess, Toast.LENGTH_SHORT).show()

        //TODO: redirigir a algún lado después de crear esa tarea, no se a donde lol igual al month view?
        navController.navigate(R.id.navigation_dayView) //cambiar a month view cuando esté hecho
    }

    /**
     * Abre el diálogo que establece el colorTag de la nueva tarea
     */
    private fun openSelectColorTagDialog() {
        val dialog = ColorSelectDialog(requireContext(),this)
        dialog.show()
    }

    /**
     * Abre el diálogo que establece el startDate de la nueva tarea
     */
    private fun openDatePickerDialogStartDate() {
        Log.i("fecha", "startdate: " + startDate)
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val formattedDate = (activity as MainActivity).simpleDateFormat.format(selectedDate.time)

                // Establecer la fecha formateada en el EditText
                startDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
                txtStartDate.setText(formattedDate)

                //Modificamos selectedDate en la activity (variable global)
                MainActivity().selectedDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
                //Log.i("PATATA", "openDatePickerDialog- SELECTED DATE: " +  MainActivity().selectedDate) //Sun Apr 21 11:21:30 GMT+02:00 2024
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    /**
     * Abre el diálogo que establece el startTime de la nueva tarea
     */
    private fun openHourPickerDialogStartTime() {
        //TODO: openHourPickerDialog

        // Obtener la hora actual del sistema
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        // Crear un TimePickerDialog para seleccionar la hora
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialogTheme,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                // Actualizar el TextView con la hora seleccionada
                startTime = LocalTime.parse(String.format("%02d:%02d", selectedHour, selectedMinute))
                txtStartTime.text = startTime.toString()
            },
            hour,
            minute,
            true // Indica si el diálogo permite elegir la hora en formato 24 horas
        )

        // Mostrar el diálogo
        timePickerDialog.show()

    }

    /**
     * Vuelve a poner el formulario en blanco
     */
    private fun cleanForm(){
        txtDesc.setText("")
        txtTitle.setText("")
        txtStartDate.setText((activity as MainActivity).selectedDate.format(MainActivity().dateTimeFormatter).toString())
        txtStartTime.text = null
        imgvwColorTagIcon.setColorFilter(Color.parseColor("#A5A7AF")) //gray2
        txtColorTagName.setText(R.string.lbl_colorTagNone)
        checkAllDay.isChecked = true
    }





    /**
     * Abre el diálogo que establece el endDate de la nueva tarea
     *
     * [FOR FUTURE UPDATE]
     */
    private fun openDatePickerDialogEndDate() {
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val formattedDate = (activity as MainActivity).simpleDateFormat.format(selectedDate.time)

                // Establecer la fecha formateada en el EditText
                endDate = formattedDate
                //txtEndDate.setText(formattedDate)

                //Modificamos selectedDate en la activity (variable global)
                MainActivity().selectedDate = LocalDate.parse(formattedDate, MainActivity().dateTimeFormatter)
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