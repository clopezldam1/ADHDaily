package com.example.adhdaily.UI.recyclers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.dialogs.ReminderPickerDialog
import com.example.adhdaily.UI.dialogs.TaskDetailsDialog
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Reminder
import com.example.adhdaily.model.entity.Task
import com.example.adhdaily.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class RemindersTaskRecycler(private val reminderList: List<Reminder>, private val taskDetailsDialog: TaskDetailsDialog, private val selectedTask: Task)
    : RecyclerView.Adapter<RemindersTaskRecycler.ViewHolder>() {

    /**
     * Inflamos la vista asociada al viewHolder (de cada item)
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_reminder_item, viewGroup, false)

        return ViewHolder(view, taskDetailsDialog, selectedTask)
    }

    /**
     * Asociar cada objeto de la taskList a nuestro viewHolder
     */
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(reminderList.isNotEmpty()) {
            val reminder: Reminder = reminderList[position]
            viewHolder.bind(reminder)
        }
    }

    /**
     * Crear el viewHolder, donde asociamos los datos
     * del objeto a los elementos de la vista
     */
    class ViewHolder(view: View, private val taskDetailsDialog: TaskDetailsDialog, private val selectedTask: Task) : RecyclerView.ViewHolder(view){
        private val txtReminderText: TextView = view.findViewById(R.id.txt_recordatorio)
        private val btnDeleteReminder: ImageButton = view.findViewById(R.id.btn_deleteReminder)
        private lateinit var context : Context

        fun bind(reminder: Reminder) {
            context = this.itemView.context

            txtReminderText.text = reminder.Text
            btnDeleteReminder.setOnClickListener{
                deleteReminder(reminder)
            }
            txtReminderText.setOnClickListener{
                Log.i("reminder", "btnOpenEditReminder.setOnClickListener: ")
                OpenEditReminder(reminder)
            }
        }

        /**
         * Abrir reminderPicker para poder editarlo
         */
        private fun OpenEditReminder(reminder: Reminder) {
            Log.i("reminder", "OpenEditReminder: OPEN EDIT ENTER" + reminder.ReminderId)
            val oldReminderDateTime = LocalDateTime.parse(reminder.DateTimeReminder)
            val reminderPickerDialog = ReminderPickerDialog(context, reminder.ReminderId, selectedTask)
            reminderPickerDialog.show()

            reminderPickerDialog.setOnDismissListener {
                onReminderDeletedOrEdited()
                val newReminderDateTime = LocalDateTime.parse(reminder.DateTimeReminder)
                updateReminderNotif(selectedTask,reminder.ReminderId,oldReminderDateTime,newReminderDateTime)
            }
        }

        /**
         * Quitar el recordatorio de la tarea y borrarlo de la base de datos,
         * después recargar el recycler de recordatorios en los detalles de la tarea
         * para mostar las modificaciones
         */
        private fun deleteReminder(reminder: Reminder) {
            val localDB = LocalDatabase.getInstance(this.context)
            //abrimos hilo para operar en base de datos
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    Log.i("reminder", "deleteReminder: id: " + reminder.ReminderId)
                    localDB.reminderDao().delete(reminder)
                } catch (ex: Exception) {
                    Log.i("CATCH", "deleteReminder: " + ex.message)
                }
            }
            Toast.makeText(context.applicationContext, R.string.toast_reminderDeletedSuccess, Toast.LENGTH_SHORT).show()
            onReminderDeletedOrEdited()

            val reminderDateTime = LocalDateTime.parse(reminder.DateTimeReminder)
            deleteReminderNotif(selectedTask, reminder.ReminderId,reminderDateTime)
        }

        /**
         * Al borrar o editar un reminder, recargamos el recycler de taskDetails
         */
         fun onReminderDeletedOrEdited() {
            taskDetailsDialog.loadRecyclerReminders()
         }

        /**
         * Actualizar notificacion al actualizar recordatorio
         */
        fun updateReminderNotif(selectedTask: Task, reminderId: Long, oldReminderDateTime: LocalDateTime, newReminderDateTime: LocalDateTime){
            val notificationHelper = NotificationHelper(context)
            notificationHelper.updateNotification(selectedTask, reminderId, oldReminderDateTime, newReminderDateTime)
        }

        /**
         * Cancelar notificacion al eliminar recordatorio
         */
        fun deleteReminderNotif( selectedTask: Task, reminderId: Long, oldReminderDateTime: LocalDateTime){
            val notificationHelper = NotificationHelper(context)
            notificationHelper.cancelNotification(selectedTask, reminderId, oldReminderDateTime)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = reminderList.size
}