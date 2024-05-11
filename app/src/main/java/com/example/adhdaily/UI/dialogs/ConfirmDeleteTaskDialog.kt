package com.example.adhdaily.UI.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.adhdaily.R
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ConfirmDeleteTaskDialog(context: Context, private val task: Task) : Dialog(context, R.style.CustomDialogTheme1) {

    val confirmDelete: Boolean = false

    var btnConfirmDelete: Button
    var btnCancel: Button
    init{
        setContentView(R.layout.dialog_confirm_delete_task)

        btnConfirmDelete = findViewById(R.id.btn_confirmDelete)
        btnConfirmDelete.setOnClickListener {
            deleteTaskFromDB()
        }

        btnCancel = findViewById(R.id.btn_cancelDelete)
        btnCancel.setOnClickListener {
            cancelDelete()
        }
    }

    /***
     * Si se le da a Cancelar, diálogo se cierra y no pasa nada
     */
    private fun cancelDelete() {
        this.dismiss()
    }

    /***
     * Si se le da a Confirmar, tarea se borra de base de datos y diálogo se cierra
     */
    private fun deleteTaskFromDB() {
        val localDB = LocalDatabase.getInstance(this.context)
        //abrimos hilo para operar en base de datos
        GlobalScope.launch(Dispatchers.IO) {
            try {
                localDB.taskDao().delete(task)
            } catch (ex:Exception) {
                Log.e("CATCH", "deleteTask: " + ex.message)
            }
        }
        this.dismiss()
        Toast.makeText(context.applicationContext, R.string.toast_taskDeletedSuccess, Toast.LENGTH_SHORT).show()
    }

}