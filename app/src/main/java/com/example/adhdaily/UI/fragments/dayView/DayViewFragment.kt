package com.example.adhdaily.UI.fragments.dayView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity

import com.example.adhdaily.databinding.FragmentDayViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DayViewFragment : Fragment() {

    private var _binding: FragmentDayViewBinding? = null

    //COMPONENTES DEL FRAGMENT:
    private lateinit var btnNextDay: ImageButton
    private lateinit var btnPreviousDay: ImageButton
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


        //TODO: hacer que al slide hacia la derecha o la izquierda, te navege por los dias (aka llamando a gotoNextDay o gotoPreviousDay)

        return root
    }

    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Rellenamos el recycler una vez creada la vista
        recyclerTaskListDayView = view.findViewById(R.id.recyclerTaskListDayView)
        loadRecyclerDayView()
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
        //TODO: gotoNextDay()
    }

    /**
     * Cambiar al día anterior (hacia la izquierda)
     */
    private fun gotoPreviousDay() {
        //TODO: gotoPreviousDay()
    }

    /**
     * Método que realiza la carga del RecyclerView (en un hilo a parte)
     */
    fun loadRecyclerDayView() {
        //Todo: loadRecyclerDayView()
        lifecycleScope.launch(Dispatchers.IO) {
            /*
            val cursosDAO = CursoDAO()
            val listaDeCursos = cursosDAO.obtenerCursosConFiltros(edbuscador.text.toString(), spinnerFiltros.selectedItem.toString(), spinnerOrdenar.selectedItem.toString())

            Log.i("BuscarFragment", "Cantidad de cursos: ${listaDeCursos.size}")

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerBuscar(listaDeCursos,requireActivity() as RecyclerBuscar.OnItemClickListener, requireActivity() as RecyclerBuscar.OnItemCheckedChangeListener,(activity as MainActivity).username)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador

                progressBar.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE

            }
             */
        }
    }


    /**
     * Método que realiza la consulta para el recyclerView
     *
     * @param curDate = fecha del día que se está visualizando
     */
    fun selectTasksOnDate(curDate: String) {
        //Todo: selectTasksOnDate()
        lifecycleScope.launch(Dispatchers.IO) {
            /*
            val cursosDAO = CursoDAO()
            val listaDeCursos = cursosDAO.obtenerCursosConFiltros(
                edbuscador.text.toString(),
                spinnerFiltros.selectedItem.toString(),
                spinnerOrdenar.selectedItem.toString()
            )

            Log.i("BuscarFragment", "Cantidad de cursos: ${listaDeCursos.size}")

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerBuscar(listaDeCursos, requireActivity() as RecyclerBuscar.OnItemClickListener, requireActivity() as RecyclerBuscar.OnItemCheckedChangeListener, (activity as MainActivity).username)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador
            }
             */
        }
    }


}