package com.example.adhdaily.UI.fragments.dayView

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.UI.recyclers.TaskListDayRecycler
import com.example.adhdaily.databinding.FragmentDayViewBinding
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class DayViewFragment : Fragment() {

    private var _binding: FragmentDayViewBinding? = null

    var selectedDate: LocalDate = MainActivity().selectedDate
    var taskList: List<Task> = emptyList()

    //COMPONENTES DEL FRAGMENT:
    private lateinit var btnNextDay: ImageButton
    private lateinit var btnNextDayTouchTarget: FrameLayout
    private lateinit var btnPreviousDay: ImageButton
    private lateinit var btnPreviousDayTouchTarget: FrameLayout
    private lateinit var txtDateHeaderDayView: TextView
    private lateinit var recyclerTaskListDayView: RecyclerView

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    /**
     * Crear la el fragmento con la vista e inicializar sus componentes
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //INFLAR VISTA:
        _binding = FragmentDayViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //INICIALIZAR COMPONENTES DE LA VISTA:
        btnNextDay = binding.imgbtnGotoNextDay
        btnNextDay.setOnClickListener {
            gotoNextDay()
        }

        btnPreviousDay = binding.imgbtnGotoPreviousDay
        btnPreviousDay.setOnClickListener{
            gotoPreviousDay()
        }

        txtDateHeaderDayView = binding.txtDateHeaderDayView
        txtDateHeaderDayView.setOnClickListener {
            openSelectDate()
        }

        // Obtén la referencia del RecyclerView desde el layout
        recyclerTaskListDayView = binding.recyclerTaskListDayView
        recyclerTaskListDayView.layoutManager = LinearLayoutManager(requireContext())


        btnPreviousDayTouchTarget = binding.touchTargetImgbtnGotoPreviousDay
        btnPreviousDayTouchTarget.setOnClickListener {
            btnPreviousDay.callOnClick()
        }

        btnNextDayTouchTarget = binding.touchTargetImgbtnGotoNextDay
        btnNextDayTouchTarget.setOnClickListener {
            btnNextDay.callOnClick()
        }

        //TODO: hacer que al slide hacia la derecha o la izquierda, te navege por los dias (aka llamando a gotoNextDay o gotoPreviousDay)

        return root
    }

    /**
     * Coloca la fecha seleccionada en la parte superior de la pantalla para mostrar sólamente esas tareas
     */
    private fun setSelectedDateOnHeader() {
        val dateFormat = DateTimeFormatter.ofPattern("dd MMM, EEEE", Locale.getDefault())
        val formattedDate = selectedDate.format(dateFormat)
        txtDateHeaderDayView.text = formattedDate
    }

    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Rellenamos el recycler una vez creada la vista
        recyclerTaskListDayView = view.findViewById(R.id.recyclerTaskListDayView)
        loadDayData()
    }

    /**
     * Al cerrar fragmento, reiniciar binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Al darle al header, abrimos el selectDateDialog para elegir abrir otro día
     * (abrimos el díalog desde MainActivity)
     */
    private fun openSelectDate() {
        (this.activity as MainActivity).openSelectDateDialog()
    }

    /**
     * Cambiar al día siguiente (hacia la derecha)
     */
    private fun gotoNextDay() {
        (activity as MainActivity).selectedDate = selectedDate.plusDays(1)
        selectedDate = (activity as MainActivity).selectedDate
        loadDayData()
    }

    /**
     * Cambiar al día anterior (hacia la izquierda)
     */
    private fun gotoPreviousDay() {
        (activity as MainActivity).selectedDate = selectedDate.minusDays(1)
        selectedDate = (activity as MainActivity).selectedDate
        loadDayData()
    }

    private fun loadDayData(){
        loadRecyclerDayView()
        setSelectedDateOnHeader()
    }

    /**
     * Método que realiza la carga del RecyclerView (consulta en un hilo a parte)
     */
    fun loadRecyclerDayView() {
        recyclerTaskListDayView.layoutManager = LinearLayoutManager(requireContext())

        //getTaskListDay()

        // Obtener la lista de tareas en un hilo de fondo
        lifecycleScope.launch {
            taskList = getTaskListDay()
            if (!taskList.isEmpty()) {
                recyclerTaskListDayView.adapter = TaskListDayRecycler(taskList)
            } else {
                //TODO: vaciar recycler aqui porfa que es cuando no hay tareas :c
                //recyclerTaskListDayView.adapter = TaskListDayRecycler(taskList)
                recyclerTaskListDayView.adapter = null
            }
        }
    }

    private suspend fun getTaskListDay(): List<Task> {
        return withContext(Dispatchers.IO) {
            val localDatabase: LocalDatabase = LocalDatabase.getInstance(requireContext())
            localDatabase.taskDao().selectTasksStartOnDate(selectedDate.toString())
        }
    }

}