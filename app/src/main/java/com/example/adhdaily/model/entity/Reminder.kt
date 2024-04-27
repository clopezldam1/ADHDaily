package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Reminder",
        foreignKeys = [ForeignKey(entity = Task::class,
                                  parentColumns = ["TaskId"],
                                  childColumns = ["TaskId_FK"],
                                  onDelete = ForeignKey.CASCADE)]
        )

data class Reminder (
    @PrimaryKey(autoGenerate = true) var ReminderId: Long,
    @ColumnInfo(name = "TaskId_FK") var TaskId_FK: Long,
    @ColumnInfo(name = "Text") var Text: String?,
    @ColumnInfo(name = "DateTime") var DateTime: String
    )
