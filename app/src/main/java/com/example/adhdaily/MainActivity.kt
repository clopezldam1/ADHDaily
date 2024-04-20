package com.example.adhdaily

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adhdaily.databinding.ActivityMainBinding
import com.example.adhdaily.ui.activities.AppSettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var btnSelectDate: ImageButton
    private lateinit var btnGotoSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //readAppConfigData()

        //Eliminar toolbar por defecto:
        supportActionBar?.hide()

        //NAVEGACIÓN BOTONES INFERIORES:
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //inicializar y gestionar eventos boton selectDate Toolbar
        btnSelectDate = binding.btnSelectDate
        btnSelectDate.setOnClickListener {
            openSelectDateDialog()
        }

        //inicializar y gestionar eventos boton gotoSettings Toolbar
        btnGotoSettings = binding.btnGotoSettings
        btnGotoSettings.setOnClickListener {
            gotoAjustes()
        }
    }

    /***
     * Abre el cuadro de diálogo que te pide seleccionar una fecha y te lleva a ella
     */
    private fun openSelectDateDialog(){
        //inflar y crear el dialog
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_select_date, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)

        //HACER LÓGICA DEL DIALOG:
        //botón de ir a la fecha (cierra dialogo y redirige)
        dialogView.findViewById<Button>(R.id.btn_gotoDate).setOnClickListener {

            Toast.makeText(this.applicationContext, "gotoDate", Toast.LENGTH_SHORT).show()

            //cerrar el dialog
            dialog.dismiss()
        }

        //mostrar el diálogo
        dialog.show()
    }

    /**
     * Método plantilla para crear un dialog (? aún no sé si esto se puede modularizar
     */
    public fun openDialog(){

    }

    /**
     * ir a la pantalla de ajustes
     */
    private fun gotoAjustes(){
        val intent = Intent(this, AppSettingsActivity::class.java)
        Toast.makeText(this.applicationContext, "switched to ajustes", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        //Cambiar transicion a la activity de forma personalizada
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**
     * Leemos los datos de la configuración de la app tanto de notificaciones como de los Settings
     */
    /*
    private fun readAppConfigData(){
     val settingsDAO = LocalDatabase.getInstance(this).settingsDao()
     val notificationsDAO = LocalDatabase.getInstance(this).notificationsDao()

     //abrimos un hilo
     lifecycleScope.launch(Dispatchers.IO) {

         //Iniciamos las variables de la activity según los datos del AppConfig
         val settings = settingsDAO.selectAll()
         if (settingsDAO.selectAll().isNotEmpty()) {
             for (setting in settings) {
                 darkMode = setting.DarkMode
                 language = setting.Idioma
                 pushNotifsActive = setting.PushNotifsActive
                 notifBarActive = setting.NotifBarActiive
                 userId = setting.UserId
             }
         }

         val notifications = notificationsDAO.selectAll()
         if (notificationsDAO.selectAll().isNotEmpty()) {
             for (notification in notifications) {
                 notifSoundActive = notification.ActivateSound
                 remindersPushActive = notification.RemindersPush
                 pendingTasksOnNotifBarActive = notification.PendingTasksOnNotifBar
                 showListOnNotifBarActive = notification.ShowListOnNotifBar
             }
         }

     }
 }
*/


}