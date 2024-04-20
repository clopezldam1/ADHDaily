package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Notifications",
        //primaryKeys = ["UserId", "Active"],
        foreignKeys = [ForeignKey(entity = Settings::class,
                                 parentColumns = ["UserId"],
                                 childColumns = ["UserId_FK"],
                                 onDelete = ForeignKey.CASCADE)]
        )
data class Notifications (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "NotifId") var NotifId: Long,

    @ColumnInfo(name = "ShowListOnNotifBar") var ShowListOnNotifBar: Boolean,
    @ColumnInfo(name = "PendingTasksOnNotifBar") var PendingTasksOnNotifBar: Boolean,
    @ColumnInfo(name = "RemindersPush") var RemindersPush: Boolean,
    @ColumnInfo(name = "ActivateSound") var ActivateSound: Boolean,

    @ColumnInfo(name = "UserId_FK") var UserId_FK: Long?
)
