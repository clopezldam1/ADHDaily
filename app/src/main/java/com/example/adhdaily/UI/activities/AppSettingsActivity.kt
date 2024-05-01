package com.example.adhdaily.UI.activities

import android.app.UiModeManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.adhdaily.R
import com.example.adhdaily.databinding.ActivityAppSettingsBinding
import com.example.adhdaily.model.database.LocalDatabase

class AppSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppSettingsBinding
    private lateinit var btnBack: ImageButton
    private lateinit var btnDeleteDb: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Eliminar toolbar por defecto:
        supportActionBar?.hide()

        //inicializar y gestionar eventos boton goBack Toolbar
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            goBack()
        }

        /*
        //TODO: UPDATE THE DARKMODE PARAMETER IN SETTINGS TABLE TO MAKE THE TOGGLE WORK
        val uiModeManager = ContextCompat.getSystemService(UI_MODE_SERVICE) as UiModeManager
        val isDarkModeEnabled = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
         */

        //inicializar y gestionar eventos boton DELETE DATABASE
        btnDeleteDb = binding.btnDeleteDB
        btnDeleteDb.setOnClickListener {
            deleteAllData()
        }
    }

    /**
     * Salir de la ventana de ajustes y volver a la principal
     */
    private fun goBack() {
        val intent = Intent(this, MainActivity::class.java)
        Toast.makeText(this.applicationContext, "retroceso", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        //Cambiar transicion a la activity de forma personalizada
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /**
     * Eliminar todos los datos de la base de datos y empezar de cero
     */
    private fun deleteAllData() {
        //TODO: deleteAllData() - show warning dialog "are you sure" to confirm, then erase (avisar de que la app se cerrará)

        val localDB = LocalDatabase.getInstance(this)
        localDB.deleteDatabase(this)
        //Cerrar aplicacion despues de borrar db
        finishAffinity()
    }
}