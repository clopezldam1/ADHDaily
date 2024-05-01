package com.example.adhdaily.model.entity

import android.icu.util.Calendar
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "Settings",
        foreignKeys = [ForeignKey(entity = NotificationSettings::class,
            parentColumns = ["NotifSettingsId"],
            childColumns = ["NotifSettingsId_FK"],
            onDelete = ForeignKey.CASCADE)]
)
data class Settings (
    @PrimaryKey @ColumnInfo(name = "UserId") var UserId: Long, //siempre será 1 bc solo hay un usuario

    @ColumnInfo(name = "DarkMode") val DarkMode: Boolean,
    @ColumnInfo(name = "LanguageTagISO") val LanguageTagISO: String, //codigo de idioma por defecto del dispositivo, ej. "es", "en"
    @ColumnInfo(name = "CalendarViewMode") val CalendarViewMode: Long, //= 1, //0=simple list, 1=progress graph
    @ColumnInfo(name = "NotifsActive") val NotifsActive: Boolean,
    @ColumnInfo(name = "DateAppInstall") val DateAppInstall: String, //fecha en la que instalas app por primera vez

    @ColumnInfo(name = "NotifSettingsId_FK") val NotifSettingsId_FK: Long? //= 1
)