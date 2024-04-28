package com.example.adhdaily.UI.fragments.newTask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.adhdaily.databinding.FragmentNewTaskBinding
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewTaskFragment : Fragment() {

    private var _binding: FragmentNewTaskBinding? = null

    //COMPONENTES DEL FRAGMENT:
    private lateinit var btnTrialTaskInsert: Button
    private lateinit var btnSelectAllTasks: Button


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


        //TODO: deshabilitar el selectDate del toolbar cuando se crea este fragment

        return root
    }


    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //TODO: volver a habilitar el selectDate del toolbar cuando se crea este fragment

    }


    private fun selectTasks() {
        var localDB = LocalDatabase.getInstance(this.requireContext())
        //var task = Task(0, etNombre.text.toString(), etEdad.text.toString().toInt(), "1233123123")

        GlobalScope.launch(Dispatchers.IO) {
            Log.i("AAAAAA", "esto no va ")
            val queryResult = localDB.taskDao().selectAllTasks()
            Log.i("AAAAA", "selectTasks: " + queryResult)
        }

    }

    //INSERT INTO Task (title, `desc`, startDate, endDate, completed) VALUES ("Titulo","Descripcion","Sun Apr 21 11:21:30 GMT+02:00 2024","Sun Apr 21 11:21:30 GMT+02:00 2024",true);
    private fun addTrialTask() {
        val localDB = LocalDatabase.getInstance(this.requireContext())
        //val newtTaskID: String = (localDB.taskDao().selectLastTaskId()[0]).toString()//consultar last ID +1
        //Log.i("PATATAAAA", "addTrialTask: TASKID NEW" + newtTaskID)
        val task = Task(7, "Tarea3", "DESC 2", "1233123123", "1233123123", false)

        Log.i("PATATA", "addTrialTask:  1 ")
        GlobalScope.launch(Dispatchers.IO) {
            Log.i("PATATA", "addTrialTask:  2 ")
            localDB.taskDao().insert(task)
            Log.i("PATATA", "addTrialTask:  3 ")
        }
    }



}