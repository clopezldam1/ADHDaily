package com.example.adhdaily.UI.fragments.taskView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.adhdaily.databinding.FragmentNewTaskBinding
import com.example.adhdaily.databinding.FragmentTaskViewBinding
import com.example.adhdaily.model.database.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskViewFragment : Fragment() {
    private var _binding: FragmentTaskViewBinding? = null

    //COMPONENTES DEL FRAGMENT:

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    /**
     * Crear la el fragmento con la vista e inicializar sus componentes
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //INFLAR VISTA:
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //INICIALIZAR COMPONENTES DE LA VISTA:

        return root
    }

    //Método para cuando se ha creado la vista, poner aquí spinners de carga
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Al cerrar fragmento, reiniciar binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}