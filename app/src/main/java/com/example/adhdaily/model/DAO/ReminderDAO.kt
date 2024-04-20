package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.Notifications
import com.example.adhdaily.model.entity.Reminder

@Dao
interface ReminderDAO {

    @Query("SELECT * FROM Reminder " +
            "INNER JOIN Task ON Task.TaskId = Reminder.TaskId_FK " +
            "WHERE Reminder.TaskId_FK LIKE :taskId")
    fun getRemindersByTaskId(taskId: Long): List<Reminder>

    @Update
    fun update(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reminder: Reminder)

    @Delete
    fun delete(reminder: Reminder)
}