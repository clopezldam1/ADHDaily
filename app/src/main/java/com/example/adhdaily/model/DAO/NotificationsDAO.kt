package com.example.adhdaily.model.DAO

import android.media.audiofx.Equalizer
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Settings

@Dao
interface NotificationsDAO {

    @Query("SELECT * FROM Notifications WHERE UserId_FK = :userId")
    fun selectByUserId(userId: Long): List<Notifications>

    @Query("SELECT NotifId FROM Notifications ORDER BY NotifId LIMIT 1")
    fun selectFirstPK(): Long

    @Query("SELECT * FROM Notifications")
    fun selectAll(): List<Notifications>

    @Update
    fun update(notifications: Notifications)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notifications: Notifications)

    @Delete
    fun delete(notifications: Notifications)
}