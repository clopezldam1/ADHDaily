package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Settings
import com.example.adhdaily.model.entity.Task

@Dao
interface SettingsDAO {

    @Query("SELECT * FROM Settings WHERE UserId = :userId")
    fun findSettingsByUserId(userId: Long): List<Settings>

    @Query("SELECT UserId FROM Settings ORDER BY UserId LIMIT 1")
    fun selectFirstPK(): Long

    @Query("SELECT * FROM Settings")
    fun selectAll(): List<Settings>

    @Update
    fun update(settings: Settings)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(settings: Settings)

    @Delete
    fun delete(settings: Settings)
}