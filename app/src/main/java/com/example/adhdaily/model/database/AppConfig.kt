package com.example.adhdaily.model.database

import android.content.Context
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Settings
import com.example.adhdaily.utils.DeviceSettings

class AppConfig {
    fun setDefaultSettings(context: Context): Settings {
        var deviceSettings= DeviceSettings();
        val localDatabase = LocalDatabase.getInstance(context)
        val notificationsDao = localDatabase.notificationsDao()
        val settingsDao = localDatabase.settingsDao()

        val defaultSettings = Settings(
            DarkMode = deviceSettings.isDeviceDarkModeEnabled(context),
            Idioma = deviceSettings.getDeviceLanguage(context),
            PushNotifsActive = true,
            NotifBarActiive = true,
            UserId = settingsDao.selectFirstPK()
        )

        val defaultNotifications = Notifications(
            NotifId = notificationsDao.selectFirstPK(),
            UserId_FK = settingsDao.selectFirstPK(),
            ShowListOnNotifBar = true,
            PendingTasksOnNotifBar = true,
            RemindersPush = true,
            ActivateSound = true
        )

        settingsDao.update(defaultSettings)
        notificationsDao.update(defaultNotifications)
        return defaultSettings
    }
}