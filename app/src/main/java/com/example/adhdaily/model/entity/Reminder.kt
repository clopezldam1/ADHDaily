package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Reminder",
        foreignKeys = [ForeignKey(entity = Task::class,
                                  parentColumns = ["TaskId"],
                                  childColumns = ["TaskId_FK"],
                                  onDelete = ForeignKey.CASCADE)]
        )

data class Reminder (
    @PrimaryKey(autoGenerate = true) var ReminderId: Long,

    @ColumnInfo(name = "Text") var Text: String,
    @ColumnInfo(name = "DateTimeReminder") var DateTimeReminder: String,
    @ColumnInfo(name = "TimeValue") var TimeValue: Long,
    @ColumnInfo(name = "TimeUnitId") var TimeUnitId: Long, //id de la lista del spinner de reminderPicker

    @ColumnInfo(name = "TaskId_FK") var TaskId_FK: Long
)
