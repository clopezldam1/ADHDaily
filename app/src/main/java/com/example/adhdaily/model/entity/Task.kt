package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Task",
        foreignKeys = [ForeignKey(entity = ColorTagsTask::class,
            parentColumns = ["ColorTagId"],
            childColumns = ["ColorTag_FK"],
            onDelete = ForeignKey.CASCADE)]
)
data class Task(
    @PrimaryKey(autoGenerate = true) var TaskId: Long,

    @ColumnInfo(name = "Title") var Title: String,
    @ColumnInfo(name = "Desc") var Desc: String?,
    @ColumnInfo(name = "StartDate") val StartDate: String,
    @ColumnInfo(name = "StartTime") val StartTime: String?, //ifnull == all day
    @ColumnInfo(name = "EndDate") val EndDate: String?,
    @ColumnInfo(name = "EndTime") val EndTime: String?, //ifnull == all day
    @ColumnInfo(name = "Completed") var Completed: Boolean = false,
    //@ColumnInfo(name = "hasReminders") val HasReminders: Boolean,
    //@ColumnInfo(name = "repeats") val Repeats: Boolean,
    //@ColumnInfo(name = "orderInDayList") val OrderInDayList: Long,
    @ColumnInfo(name = "ColorTag_FK") val ColorTag_FK: Long = 1 //1=transparent, 2=blue, 3=green, 4=purple, 5=yellow, 6=red
)