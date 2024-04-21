package com.example.adhdaily.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.example.adhdaily.R
import com.example.adhdaily.databinding.ActivityAppSettingsBinding

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppSettingsBinding
    private lateinit var btnBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Eliminar toolbar por defecto:
        supportActionBar?.hide()

        //inicializar y gestionar eventos boton gotoSettings Toolbar
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        val intent = Intent(this, MainActivity::class.java)
        Toast.makeText(this.applicationContext, "retroceso", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        //Cambiar transicion a la activity de forma personalizada
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}