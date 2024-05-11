package com.example.adhdaily.model.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.adhdaily.model.entity.Task
import java.sql.Date
import java.time.LocalDate


@Dao
interface TaskDAO {

    @Query("SELECT * FROM Task;")
    fun selectAllTasks(): List<Task>

    @Query("SELECT * FROM Task WHERE DATE(StartDate) = date('now');")
    fun selectTasksStartToday(): List<Task>

    @Query("SELECT * FROM Task WHERE DATE(StartDate) = date(:date);")
    fun selectTasksStartOnDate(date: String): List<Task>

    @Query("SELECT TaskId FROM Task ORDER BY TaskId DESC LIMIT 1;")
    fun selectLastTaskId(): Long
    @Query("SELECT * FROM Task WHERE TaskId = :taskId;")
    fun selectTaskById(taskId: Long): Task

    /*
    //Cuando le pasas una fecha, te devuelve todas las tasks evistentes que pasan por esa fecha
    @Query("SELECT * FROM Task WHERE DATE(:fecha) BETWEEN DATE(StartDate) AND DATE(EndDate);")
    fun selectActiveTasksOnDate(fecha: String): List<Task>
    //SELECT * FROM Task WHERE DATE(startDate) BETWEEN DATE('2022-04-01') AND DATE('2022-05-01')

    @Query("SELECT * FROM Task WHERE Title LIKE :titulo;")
    fun selectTaskByTitle(titulo: String): List<Task>
    */

    @Update
    fun update(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //SI PONES ESTO, CUANDO LOS ID COINCIDAN NO PASA AL SIGUIENTE, LO SOBRESCRIBE
    fun insert(task: Task): Long
    //INSERT INTO Task (Title, `Desc`, StartDate, StartTime, EndDate, EndTime, completed, ColorTag_FK) VALUES ("Titulo","Descripcion","2024-05-01","11:21",null, null, true, 3);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(tasks: List<Task>)

    @Delete
    fun delete(task: Task)
}