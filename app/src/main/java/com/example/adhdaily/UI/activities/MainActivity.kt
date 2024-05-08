package com.example.adhdaily.UI.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //Toolbar
    lateinit var btnSelectDate: ImageButton
    private lateinit var btnGotoSettings: ImageButton

    //Current date
    var dateFormatPattern = "dd/MM/yyyy" //Cambiar el formato de la fecha aquí //TODO: Incluir ajuste para cambiar el date format entre europeo y americano
    val simpleDateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault()) //Locale.getDefault gets the timezone
    val dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatPattern)
    val today = LocalDate.now().format(dateTimeFormatter)
    var selectedDate: LocalDate = LocalDate.parse(today, dateTimeFormatter) //por defecto, al abrir la app está seleccionada la fecha de hoy


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
        btnSelectDate = binding.btnSelectDate
        if (::btnSelectDate.isInitialized) {
            btnSelectDate.setOnClickListener {
                openSelectDateDialog()
            }
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
    fun openSelectDateDialog(){
        val dialogFragment = SelectDateDialog(this)
        dialogFragment.show()
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

    //TODO: convertir en notificacion emergente
    //TODO: hacer que sean stackables (same group)
    //TODO: hacer que tengan un botón de marcar como hecho (IMPORTANT)
    //TODO: hacer que tengan otro boton para aplazar el reminder
    //Crear nueva notificación de recordatorio
    fun createReminderNotif(taskTitle: String, taskDesc: String) {
        var builder = NotificationCompat.Builder(this, "com.example.adhdaily.reminders")
            .setSmallIcon(R.drawable.app_icon_nobg)
            .setColorized(true)
            .setContentTitle(taskTitle)
            .setContentText(taskDesc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setGroup("reminders")
            //.setGroupSummary(true)

        //Comprobar que tenemos permiso de notifs concedido
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 123)
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, builder.build())
        }
    }

    //Crear canal para notificiones para recordatorios
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is not in the Support Library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ADHDailyReminders"
            val descriptionText = "Calendario para gestionar tareas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("com.example.adhdaily.reminders", name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.BLUE
            }

            //NotificationChannelGroup("com.example.adhdaily.reminders", "reminders")

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