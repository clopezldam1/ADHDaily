package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "NotificationSettings",

        )
data class NotificationSettings (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "NotifSettingsId") var NotifSettingsId: Long,

    @ColumnInfo(name = "ShowListOnNotifBar") var ShowListOnNotifBar: Boolean,
    @ColumnInfo(name = "PendingTasksOnNotifBar") var PendingTasksOnNotifBar: Boolean,
    @ColumnInfo(name = "RemindersPush") var RemindersPush: Boolean,
    @ColumnInfo(name = "ActivateSound") var ActivateSound: Boolean,
)
