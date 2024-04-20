package com.example.adhdaily.utils

import android.content.Context
import android.content.res.Configuration
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Settings
import java.util.Locale

/***
 * Desde esta clase vamos a poder leer la configuración del dispositivo
 */
class DeviceSettings {



     fun getDeviceLanguage(context: Context): String {
        val locale: Locale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return locale.language
    }

    public fun isDeviceDarkModeEnabled(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}