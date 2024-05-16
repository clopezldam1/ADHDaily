package com.example.adhdaily.UI.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.adhdaily.R
import com.example.adhdaily.UI.dialogs.SelectDateDialog
import com.example.adhdaily.databinding.ActivityMainBinding
import com.example.adhdaily.utils.NotificationHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    //Toolbar
    lateinit var btnSelectDate: ImageButton
    private lateinit var btnGotoSettings: ImageButton

    //Current date
    var dateFormatPattern = "dd/MM/yyyy" //Cambiar el formato de la fecha aquí //TODO: Incluir ajuste para cambiar el date format entre europeo y americano
    val simpleDateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault()) //Locale.getDefault gets the timezone
    val dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatPattern)
    val today = LocalDate.now().format(dateTimeFormatter)
    var selectedDate: LocalDate = LocalDate.parse(today, dateTimeFormatter) //por defecto, al abrir la app está seleccionada la fecha de hoy
    var time24hFormat: Boolean = true //by default, times are in 24h format

    val defaultStartTimeOfTask: LocalTime = LocalTime.MIDNIGHT //si una tarea no tiene hora de inicio, se entenderá como que empieza a las 00:00 (recordatorios se settean según esto)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //readAppConfigData()

        //Crear canal para las notificaciones push
        createNotificationChannel()

        //Eliminar toolbar por defecto:
        supportActionBar?.hide()

        //NAVEGACIÓN BOTONES INFERIORES:
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_calendarView,
                R.id.navigation_newTaskView,
                R.id.navigation_dayView
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //inicializar y gestionar eventos boton selectDate Toolbar
        //para abrir y navegar desde el dialog
        btnSelectDate = binding.btnSelectDate
        btnSelectDate.setOnClickListener {
            openSelectDateDialog()
        }

        //inicializar y gestionar eventos boton gotoSettings Toolbar
        btnGotoSettings = binding.btnGotoSettings
        btnGotoSettings.setOnClickListener {
            gotoAjustes()
        }

        //Comprobar que tenemos permiso de notifs concedido
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS), 123
                )
            }
        }

    }

    /***
     * Abre el cuadro de diálogo que te pide seleccionar una fecha y te lleva a ella
     */
    fun openSelectDateDialog(){
        val dialog = SelectDateDialog(this)
        dialog.show()

        dialog.setOnDismissListener {
            selectedDate = LocalDate.parse(dialog.txtSelectDate.text, dateTimeFormatter)
            navController.navigate(R.id.navigation_dayView)
        }

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
     * Crear canal para notificiones para recordatorios
     */
    private fun createNotificationChannel() {
        //val notificationHelper = NotificationHelper(this)
        //notificationHelper.createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = this.resources.getString(R.string.notifChannelNameReminders)
            val descriptionText = this.resources.getString(R.string.notifChannelDescReminders)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel =
                NotificationChannel("com.example.adhdaily.reminders", name, importance).apply {
                    description = descriptionText
                    enableLights(true)
                    lightColor = Color.BLUE
                }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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