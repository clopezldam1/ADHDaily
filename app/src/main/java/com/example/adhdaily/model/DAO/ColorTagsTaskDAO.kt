package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.adhdaily.model.entity.ColorTagsTask
import com.example.adhdaily.model.entity.Task
import java.sql.Date


@Dao
interface ColorTagsTaskDAO {
    @Query("SELECT * FROM ColorTagsTask WHERE ColorTagId = :colorTagId;")
    fun selectColorTagById(colorTagId: Long): ColorTagsTask

    @Query("SELECT * FROM ColorTagsTask;")
    fun selectAllColorTags(): List<ColorTagsTask>

    @Update
    fun update(colorTag: ColorTagsTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //SI PONES ESTO, CUANDO LOS ID COINCIDAN NO PASA AL SIGUIENTE, LO SOBRESCRIBE
     fun insert(colorTag: ColorTagsTask): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(colorTag: List<ColorTagsTask>)

    @Delete
    fun delete(colorTag: ColorTagsTask)
}