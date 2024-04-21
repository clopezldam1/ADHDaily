package com.example.adhdaily.UI.fragments.dayView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.adhdaily.databinding.FragmentDayViewBinding


/**
 * A simple [Fragment] subclass.
 * Use the [DayViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayViewFragment : Fragment() {

    private var _binding: FragmentDayViewBinding? = null

    //COMPONENTES DEL FRAGMENT:


    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //INFLAR VISTA:
        _binding = FragmentDayViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //INICIALIZAR COMPONENTES DE LA VISTA:


        return root
    }

    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}