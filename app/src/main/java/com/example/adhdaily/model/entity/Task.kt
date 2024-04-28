package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true) var TaskId: Long,
    @ColumnInfo(name = "title") var Title: String,
    @ColumnInfo(name = "desc") var Desc: String?,
    @ColumnInfo(name = "startDate") val StartDate: String,
    @ColumnInfo(name = "startTime") val StartTime: String?, //ifnull == all day
    @ColumnInfo(name = "endDate") val EndDate: String?,
    @ColumnInfo(name = "endTime") val EndTime: String?,
    @ColumnInfo(name = "completed") val Completed: Boolean = false
)