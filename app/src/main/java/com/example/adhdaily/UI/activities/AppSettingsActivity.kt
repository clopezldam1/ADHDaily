package com.example.adhdaily.UI.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.adhdaily.R
import com.example.adhdaily.databinding.ActivityAppSettingsBinding
import com.example.adhdaily.model.database.LocalDatabase


class AppSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppSettingsBinding
    private lateinit var btnBack: ImageButton
    private lateinit var linkPrivacyPolicy: TextView
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

        linkPrivacyPolicy = binding.txtSettingsFooterPrivacyPolicy
        linkPrivacyPolicy.setPaintFlags(linkPrivacyPolicy.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG) //underline text
        linkPrivacyPolicy.setOnClickListener {
            gotoPrivacyPolicy()
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
        startActivity(intent)
        //Cambiar transicion a la activity de forma personalizada
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    /**
     * Redirect link to view privacy policy
     */
    private fun gotoPrivacyPolicy() {
        var url = "https://docs.google.com/document/d/1mTlu6-hdVZo2u_F0KsLPKP_OiEEL2U57/edit?usp=sharing&ouid=112296565227382915150&rtpof=true&sd=true"
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
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