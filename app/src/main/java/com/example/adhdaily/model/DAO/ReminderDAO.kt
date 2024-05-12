package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Task

@Dao
interface ReminderDAO {

    /*
    @Query("SELECT * FROM Reminder " +
            "INNER JOIN Task ON Task.TaskId = Reminder.TaskId_FK " +
            "WHERE Reminder.TaskId_FK LIKE :taskId")
    fun getRemindersByTaskId(taskId: Long): List<Reminder>
    */

    @Query("SELECT * FROM Reminder WHERE TaskId_FK = :taskId;")
    fun selectRemindersOnTask(taskId: Long): List<Reminder>

    @Query("SELECT * FROM Reminder WHERE ReminderId = :reminderId;")
    fun selectReminderById(reminderId: Long): Reminder

    @Query("SELECT * FROM Reminder;")
    fun selectAllReminders(): List<Reminder>

    @Update
    fun update(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reminder: Reminder)

    @Query("INSERT INTO Reminder (Text, DateTimeReminder, TimeValue, TimeUnitId, TaskId_FK) VALUES (:reminderText, :reminderDateTime, :timeValue, :timeUnitId, :taskId);")
    fun insertReminderIntoTask(reminderText: String, reminderDateTime: String, timeValue: Long, timeUnitId: Long, taskId: Long)
    //INSERT INTO Reminder (Text, DateTime, TaskId_FK) VALUES ("10 mins antes","2024-05-01 11:21:00", 18);

    @Delete
    fun delete(reminder: Reminder)

    @Query("SELECT * FROM Reminder WHERE ReminderId = :reminderId;")
    fun deleteReminderById(reminderId: Long): Reminder

}