package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Settings")
data class Settings (
    @PrimaryKey(autoGenerate = true) var UserId: Long,
    @ColumnInfo(name = "DarkMode") val DarkMode: Boolean?,
    @ColumnInfo(name = "Idioma") val Idioma: String,
    @ColumnInfo(name = "PushNotifsActive") val PushNotifsActive: Boolean,
    @ColumnInfo(name = "NotifBarActive") val NotifBarActiive: Boolean,


)