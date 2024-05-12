package com.example.adhdaily.model.database

import android.content.Context
import android.icu.util.ULocale
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
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
        version = 8)

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

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Reminder RENAME COLUMN Title TO Text")
            }
        }
        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Reminder RENAME COLUMN DateTime TO DateTimeReminder")
                database.execSQL("ALTER TABLE Reminder ADD COLUMN TimeValue INTEGER")
                database.execSQL("ALTER TABLE Reminder ADD COLUMN TimeUnitId INTEGER")
            }
        }

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val databaseFile = context.getDatabasePath(DB_ADHDAILY)
                if (databaseFile.exists()) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        DB_ADHDAILY
                    )
                    //.addMigrations(MIGRATION_7_8)
                    .build()
                } else {
                    // La base de datos no existe, crearla
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        DB_ADHDAILY
                    )
                        .fallbackToDestructiveMigration() // En vez de hacer migración, destruyes lo anterior
                        //.addTypeConverter(TypeConverters())
                        .build()

                    // Añadir datos por defecto una vez creada BD
                    GlobalScope.launch(Dispatchers.IO) {
                        INSTANCE?.loadDefaultDatabaseData(INSTANCE!!)
                    }
                }
                INSTANCE!!
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
        colorTagsDao.insert(ColorTagsTask(1, "None",null,"#00FFFFFF"))
        colorTagsDao.insert(ColorTagsTask(2, "Blue","task_color_tag_blue","#7F97AD"))
        colorTagsDao.insert(ColorTagsTask(3, "Green","task_color_tag_green","#82A176"))
        colorTagsDao.insert(ColorTagsTask(4, "Purple","task_color_tag_purple","#856C91"))
        colorTagsDao.insert(ColorTagsTask(5, "Yellow","task_color_tag_yellow","#BFBA70"))
        colorTagsDao.insert(ColorTagsTask(6, "Red","task_color_tag_red","#B77663"))


        //Inicializar configuración de las nofticaciones (NotificationSettings)
        val defaultNotifications = NotificationSettings(
            NotifSettingsId = 1,
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
            UserId = 1,
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