package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.NotificationSettings

@Dao
interface NotificationSettingsDAO {

    @Query("SELECT NotifSettingsId FROM NotificationSettings ORDER BY NotifSettingsId DESC LIMIT 1")
    fun selectLastPK(): Long

    @Query("SELECT * FROM NotificationSettings")
    fun selectAll(): List<NotificationSettings>

    @Update
    fun update(notificationSettings: NotificationSettings)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationSettings: NotificationSettings)

    @Delete
    fun delete(notificationSettings: NotificationSettings)
}