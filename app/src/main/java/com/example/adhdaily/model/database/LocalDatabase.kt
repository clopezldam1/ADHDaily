package com.example.adhdaily.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.adhdaily.model.DAO.NotificationsDAO
import com.example.adhdaily.model.DAO.ReminderDAO
import com.example.adhdaily.model.DAO.SettingsDAO
import com.example.adhdaily.model.DAO.TaskDAO
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Settings
import com.example.adhdaily.model.entity.Task
import com.example.adhdaily.utils.DeviceSettings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Task::class, Settings::class, Reminder::class, Notifications::class],
        version = 2)
//@TypeConverters(TypeConverters::class)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun reminderDao(): ReminderDAO
    abstract fun notificationsDao(): NotificationsDAO

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
                        /*
                        // Insertar datos por defecto al crear la base de datos
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                // Definir los valores predeterminados para Settings
                                val appConfig = AppConfig();
                                val defaultSettings = appConfig.setDefaultSettings(context)

                                GlobalScope.launch {
                                    // Insertar tus datos aquí utilizando el DAO
                                    val settingsDao = instance?.settingsDao()
                                    settingsDao?.insert(defaultSettings)
                                }
                            }
                        })
                        */
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}