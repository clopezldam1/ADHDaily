package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.adhdaily.model.entity.Task


@Dao
interface TaskDAO {
    @Query("SELECT * FROM Task")
    fun selectAllTasks(): List<Task>

    /*
    //Cuando le pasas una fecha, te devuelve todas las tasks evistentes que pasan por esa fecha
    @Query("SELECT * FROM Task WHERE :fecha BETWEEN StartDate AND EndDate")
    fun selectActiveTasksOnDate(fecha: String): List<Task>


    @Query("SELECT * FROM Task WHERE Title LIKE :titulo")
    fun selectTaskByTitle(titulo: String): List<Task>
     */

    @Update
    fun update(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Insert
    fun insertAll(tasks: Task)

    @Delete
    fun delete(task: Task)
}