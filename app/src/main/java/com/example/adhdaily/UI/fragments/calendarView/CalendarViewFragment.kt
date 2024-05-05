package com.example.adhdaily.UI.fragments.calendarView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.databinding.FragmentCalendarViewBinding
import java.util.Calendar

class CalendarViewFragment : Fragment() {

    private var _binding: FragmentCalendarViewBinding? = null

    //COMPONENTES DEL FRAGMENT:
    private lateinit var calendarView:  CalendarView
    private lateinit var txt_monthHeaderMonthView:  TextView
    private lateinit var txt_yearHeaderMonthView:  TextView
    private lateinit var imgbtn_gotoPreviousMonth:  ImageButton
    private lateinit var imgbtn_gotoNextMonth:  ImageButton
    private lateinit var layout_openSelectDateDialog:  LinearLayout


    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //INFLAR VISTA:
        _binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //INICIALIZAR COMPONENTES DE LA VISTA:
        calendarView = binding.calendarView
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            txt_monthHeaderMonthView.setText((month + 1).toString())
            txt_yearHeaderMonthView.setText(year.toString())
        }

        txt_monthHeaderMonthView = binding.txtMonthHeaderMonthView
        txt_yearHeaderMonthView = binding.txtYearHeaderMonthView

        imgbtn_gotoPreviousMonth = binding.imgbtnGotoPreviousMonth
        imgbtn_gotoPreviousMonth.setOnClickListener {
            gotoPreviousMonth()
        }
        imgbtn_gotoNextMonth = binding.imgbtnGotoNextMonth
        imgbtn_gotoNextMonth.setOnClickListener {
            gotoNextMonth()
        }

        layout_openSelectDateDialog = binding.layoutOpenSelectDateDialog
        layout_openSelectDateDialog.setOnClickListener {
           openSelectDateDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //TODO:  fix gotoPreviousMonth (el paso de un mes a otro con las flechas de arriba lo hace mal)
    private fun gotoNextMonth() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1) // Avanzar al mes siguiente

        val nextMonth = calendar.timeInMillis
        calendarView.date = nextMonth // Actualizar el CalendarView al mes siguiente
    }

    //TODO: fix gotoPreviousMonth (el paso de un mes a otro con las flechas de arriba lo hace mal)
    private fun gotoPreviousMonth() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1) // Retroceder un mes

        val nextMonth = calendar.timeInMillis
        calendarView.date = nextMonth // Actualizar el CalendarView al mes siguiente

    }

    /**
     * Abre el diálogo de seleccionar fecha y te lleva a la seleccionada
     */
    private fun openSelectDateDialog() {
        (this.activity as MainActivity).openSelectDateDialog()
    }


}