package com.example.adhdaily.UI.fragments.newTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.UI.dialogs.ColorSelectDialog
import com.example.adhdaily.databinding.FragmentNewTaskBinding
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale


class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null

    //COMPONENTES DEL FRAGMENT:
    private lateinit var btnTrialTaskInsert: Button
    private lateinit var btnSelectAllTasks: Button
    private lateinit var layoutSelectColor: LinearLayout

    lateinit var imgvwColorTagIcon: ImageView
    lateinit var txtColorTagName: TextView

    //VARIABLES DEL FRAGMENT:
    var titulo: String = ""
    var descripcion: String? = null
    var startDate: String = LocalDate.now().toString()
    var startTime: String? = null
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
        btnSelectAllTasks = binding.btnSelectAllTasks
        btnSelectAllTasks.setOnClickListener {
            selectTasks()
        }
        btnTrialTaskInsert = binding.btnAddTrialTask
        btnTrialTaskInsert.setOnClickListener {
            addTrialTask()
        }

        layoutSelectColor = binding.layoutOpenColorTagDialog
        layoutSelectColor.setOnClickListener {
            openSelectColorTagDialog()
        }

        imgvwColorTagIcon = binding.imgvwColorTagIcon
        txtColorTagName = binding.txtColorTagName

        //TODO: deshabilitar el selectDate del toolbar cuando se crea este fragment

        return root
    }

    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //deshabilitar el boton selectDate del toolbar cuando se cierra este fragment
        (activity as MainActivity).btnSelectDate.isEnabled = false
        (activity as MainActivity).btnSelectDate.alpha = 0.5f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        //volver a habilitar el boton selectDate del toolbar cuando se cierra este fragment
        (activity as MainActivity).btnSelectDate.isEnabled = true
        (activity as MainActivity).btnSelectDate.alpha = 1f
    }

    private fun openSelectColorTagDialog() {
        val dialog = ColorSelectDialog(requireContext(),this)
        dialog.show()
    }


    private fun selectTasks() {
        var localDB = LocalDatabase.getInstance(this.requireContext())
        //var task = Task(0, etNombre.text.toString(), etEdad.text.toString().toInt(), "1233123123")

        GlobalScope.launch(Dispatchers.IO) {
            val queryResult = localDB.taskDao().selectAllTasks()
            Log.i("AAAAA", "selectTasks: " + queryResult)
        }

        Log.i("FechaAct", "onCreate: MAINACTIVITY SELECTED DATE: " + MainActivity().selectedDate)
    }

    //INSERT INTO Task (title, `desc`, startDate, endDate, completed) VALUES ("Titulo","Descripcion","Sun Apr 21 11:21:30 GMT+02:00 2024","Sun Apr 21 11:21:30 GMT+02:00 2024",true);
    private fun addTrialTask() {
        val localDB = LocalDatabase.getInstance(this.requireContext())

        //Abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("PATATAAAA", "addTrialTask:  LocalDateTime.now()" +  Locale.getDefault())

            val newtTaskID = localDB.taskDao().selectLastTaskId() + 1 //consultar last ID +1
            try {
                val task = Task(newtTaskID, "Tarea3", "DESC 2", "2022-04-22", null,"2022-04-22" ,"10:34", false, 2)
                localDB.taskDao().insert(task)
            }catch (ex:Exception){
                Log.i("CATCH", "addTrialTask: " + ex.message)
            }

        }
    }
}