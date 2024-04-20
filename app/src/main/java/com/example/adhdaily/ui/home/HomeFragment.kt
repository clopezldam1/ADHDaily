package com.example.adhdaily.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.adhdaily.R
import com.example.adhdaily.databinding.FragmentHomeBinding
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var btn: Button

    // This property is only valid between onCreateView and  onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        btn = root.findViewById(R.id.btn)
        btn.setOnClickListener {
            insertTask();
        }
        return root
    }

    private fun insertTask() {

        println("GUARDANDO")

        var localDB = LocalDatabase.getInstance(this.requireContext())
        var task = Task(0, "AAA", null, Locale.getDefault().toString(), null, false )


        GlobalScope.launch(Dispatchers.IO) {
            localDB.taskDao().insert(task)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}