package com.example.adhdaily.adapters

import android.content.Context
import androidx.room.util.query
import com.example.adhdaily.model.DAO.TaskDAO
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskAdapter {
    fun selectAllTasks(context: Context): List<Task> {
        var queryResult: List<Task>? = null
        var localDB = LocalDatabase.getInstance(context)
        //var usuario = Usuario(0, etNombre.text.toString(), etEdad.text.toString().toInt(), "1233123123")

       /*
        GlobalScope.launch(Dispatchers.IO) {
            queryResult = localDB.taskDao().selectAllTasks()
        }
        */

        return localDB.taskDao().selectAllTasks()
    }

     fun selectAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

     fun update(task: Task) {
        TODO("Not yet implemented")
    }

    //INSERT INTO Task (title, `desc`, startDate, endDate, completed) VALUES ("Titulo","Descripcion","Sun Apr 21 11:21:30 GMT+02:00 2024","Sun Apr 21 11:21:30 GMT+02:00 2024",true);
     fun insert(task: Task) {
        val title = task.Title
        val desc = task.Desc
    }

    fun insertAll(tasks: Task) {
        TODO("Not yet implemented")
    }

     fun delete(task: Task) {
        TODO("Not yet implemented")
    }

}