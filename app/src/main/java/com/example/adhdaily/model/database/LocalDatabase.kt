package com.example.adhdaily.model.database

import android.content.Context
import android.icu.util.ULocale
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adhdaily.UI.activities.AppSettingsActivity
import com.example.adhdaily.model.DAO.ColorTagsTaskDAO
import com.example.adhdaily.model.DAO.NotificationSettingsDAO
import com.example.adhdaily.model.DAO.ReminderDAO
import com.example.adhdaily.model.DAO.SettingsDAO
import com.example.adhdaily.model.DAO.TaskDAO
import com.example.adhdaily.model.entity.ColorTagsTask
import com.example.adhdaily.model.entity.NotificationSettings
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Settings
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

@Database(entities = [Task::class, Settings::class, Reminder::class, NotificationSettings::class, ColorTagsTask::class],
        version = 5)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun reminderDao(): ReminderDAO
    abstract fun notificationSettingsDao(): NotificationSettingsDAO
    abstract fun colorTagsTaskDao(): ColorTagsTaskDAO

    companion object {
        private const val DB_ADHDAILY = "ADHDaily.db"

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        DB_ADHDAILY)
                        .fallbackToDestructiveMigration() //en vez de hacer migracion, destruyes lo anterior
                        //.addTypeConverter(TypeConverters())
                        .build()

                    INSTANCE = instance

                    //Añadir datos por defecto una vez creada BD
                    GlobalScope.launch(Dispatchers.IO) {
                        instance.loadDefaultDatabaseData(instance)
                    }
                }
                return instance
            }
        }
    }

    /**
     * Al crear la base de datos por primera vez, cuando instalamos la aplicación,
     * insertamos todos los registros por defecto que necesitamos
     *
     * @param localDatabase -> instancia de la base de datos local ROOM
     */
    fun loadDefaultDatabaseData(localDatabase: LocalDatabase){
        val colorTagsDao = localDatabase.colorTagsTaskDao()
        val notificationSettingsDao = localDatabase.notificationSettingsDao()
        val settingsDao = localDatabase.settingsDao()

        //Inicializamos todas las opciones de colorTags
        colorTagsDao.insert(ColorTagsTask(1, "None",null))
        colorTagsDao.insert(ColorTagsTask(2, "Blue","task_color_tag_blue.png"))
        colorTagsDao.insert(ColorTagsTask(3, "Green","task_color_tag_green.png"))
        colorTagsDao.insert(ColorTagsTask(4, "Purple","task_color_tag_purple.png"))
        colorTagsDao.insert(ColorTagsTask(5, "Yellow","task_color_tag_yellow.png"))
        colorTagsDao.insert(ColorTagsTask(6, "Red","task_color_tag_red.png"))


        //Inicializar configuración de las nofticaciones (NotificationSettings)
        val defaultNotifications = NotificationSettings(
            NotifSettingsId = 0, //como es autoincrement, ponemos 0 para que lo pise con lo que sea
            ShowListOnNotifBar = true,
            PendingTasksOnNotifBar = true,
            RemindersPush = true,
            ActivateSound = true
        )
        notificationSettingsDao.insert(defaultNotifications)


        //Inicializar configuración de fábrica de la app (AppSettings)
        var useDarkMode = false
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            useDarkMode = true
        }
        val defaultSettings = Settings(
            UserId = 0, //como es autoincrement, ponemos 0 para que lo pise con lo que sea
            DarkMode = useDarkMode, //no obstante, por defecto app sigue UI del dispositivo a no ser que user lo cambie manualmente
            LanguageTagISO = ULocale.getDefault().language,
            CalendarViewMode = 1, //0=simple list, 1=progress graph
            NotifsActive = true,
            DateAppInstall = Date().toString(),
            NotifSettingsId_FK = notificationSettingsDao.selectLastPK()
        )
        settingsDao.insert(defaultSettings)
    }


    /**
     * Eliminar base de datos con todos sus datos. La próxima vez que se abra la aplicación, se volverá a crear de cero con los valores por defecto
     *
     * @param context -> context of an activity
     */
    fun deleteDatabase(context: Context){
        context.deleteDatabase("ADHDaily.db")
    }
}