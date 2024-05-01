package com.example.adhdaily.utils

import android.content.Context
import com.example.adhdaily.model.entity.NotificationSettings
import com.example.adhdaily.model.entity.Settings
import com.example.adhdaily.utils.DeviceSettings

class AppConfig {
    /*
    fun setDefaultSettings(context: Context): Settings {
        var deviceSettings= DeviceSettings();

        val localDatabase = LocalDatabase.getInstance(context)
        val notificationsDao = localDatabase.notificationSettingsDao()
        val settingsDao = localDatabase.settingsDao()

        val defaultSettings = Settings(
            DarkMode = deviceSettings.isDeviceDarkModeEnabled(context),
            Idioma = deviceSettings.getDeviceLanguage(context),
            PushNotifsActive = true,
            NotifBarActiive = true,
            UserId = settingsDao.selectLastPK()
        )

        val defaultNotificationSettings = NotificationSettings(
            NotifId = notificationsDao.selectLastPK(),
            UserId_FK = settingsDao.selectLastPK(),
            ShowListOnNotifBar = true,
            PendingTasksOnNotifBar = true,
            RemindersPush = true,
            ActivateSound = true
        )

        settingsDao.update(defaultSettings)
        notificationsDao.update(defaultNotificationSettings)

        return defaultSettings
    }
     */
}