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
import androidx.viewpager.widget.ViewPager
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.UI.dialogs.TaskDetailsDialog
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


class DayViewFragment : Fragment(){

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
    //private lateinit var viewPagerDeslizar: ViewPager --> FUTURE UPDATE

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
        btnNextDayTouchTarget = binding.touchTargetImgbtnGotoNextDay
        btnNextDayTouchTarget.setOnClickListener {
            btnNextDay.callOnClick()
        }

        btnPreviousDay = binding.imgbtnGotoPreviousDay
        btnPreviousDay.setOnClickListener{
            gotoPreviousDay()
        }
        btnPreviousDayTouchTarget = binding.touchTargetImgbtnGotoPreviousDay
        btnPreviousDayTouchTarget.setOnClickListener {
            btnPreviousDay.callOnClick()
        }

        txtDateHeaderDayView = binding.txtDateHeaderDayView
        txtDateHeaderDayView.setOnClickListener {
            openSelectDate()
        }

        //inicializar recycler en layout
        recyclerTaskListDayView = binding.recyclerTaskListDayView
        recyclerTaskListDayView.layoutManager = LinearLayoutManager(requireContext())

        //TODO: hacer que al slide hacia la derecha o la izquierda, te navege por los dias (aka llamando a gotoNextDay o gotoPreviousDay)
        /*
        viewPagerDeslizar = binding.viewPager
        var previousPosition = 0
        viewPagerDeslizar.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Este método se llama cuando se está desplazando entre páginas
            }

            override fun onPageSelected(position: Int) {
                // Este método se llama cuando se selecciona una nueva página
                if (position > previousPosition) {
                    // El usuario ha deslizado hacia la derecha
                    btnNextDay.callOnClick()
                } else if (position < previousPosition) {
                    // El usuario ha deslizado hacia la izquierda
                    btnPreviousDay.callOnClick()
                }

                // Actualizar la posición anterior
                previousPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        */

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

    /**
     * Cuando ya se ha creado la vista, cargamos el recycler
     */
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

    /**
     * Siempre que se vuelva a este fragmento, actualizar la fecha seleccionada
     * y volver a cargar el recycler en relación a ésta
     */
    override fun onResume() {
        super.onResume()

        selectedDate = (activity as MainActivity).selectedDate
        loadDayData()
    }

    /**
     * Cuando se cambia de día, se modifica el header y se vuelve a recargar el recycler
     */
    fun loadDayData(){
        loadRecyclerDayView()
        setSelectedDateOnHeader()
    }

    /**
     * Método que realiza la carga del RecyclerView (consulta en un hilo a parte)
     */
    private fun loadRecyclerDayView() {
        //recyclerTaskListDayView.layoutManager = LinearLayoutManager(requireContext())
        val dayViewFragment: DayViewFragment = this

        // Obtener la lista de tareas en un hilo de fondo
        lifecycleScope.launch {
            taskList = getTaskListDay()
            if (!taskList.isEmpty()) {
                recyclerTaskListDayView.adapter = TaskListDayRecycler(taskList, dayViewFragment)
            } else {
                recyclerTaskListDayView.adapter = null
            }
        }
    }

    /**
     * Realizar consulta para obtener la lista de tareas del día (ejecutar en hilo a parte)
     */
    private suspend fun getTaskListDay(): List<Task> {
        return withContext(Dispatchers.IO) {
            val localDatabase: LocalDatabase = LocalDatabase.getInstance(requireContext())
            localDatabase.taskDao().selectTasksStartOnDate(selectedDate.toString())
        }
    }

}