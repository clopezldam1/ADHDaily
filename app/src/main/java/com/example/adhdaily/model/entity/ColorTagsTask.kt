package com.example.adhdaily.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ColorTagsTask")
data class ColorTagsTask(
    @PrimaryKey(autoGenerate = true) var ColorTagId: Long,
    @ColumnInfo(name = "name") var Name: String,
    @ColumnInfo(name = "filename") var filename: String?
)