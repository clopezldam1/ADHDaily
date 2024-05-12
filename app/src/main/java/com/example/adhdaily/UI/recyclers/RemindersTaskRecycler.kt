package com.example.adhdaily.UI.recyclers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.model.entity.Reminder

class RemindersTaskRecycler(private val reminderList: List<Reminder>) : RecyclerView.Adapter<RemindersTaskRecycler.ViewHolder>() {
    /**
     * Inflamos la vista asociada al viewHolder (de cada item)
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_reminder_item, viewGroup, false)

        return ViewHolder(view)
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
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtReminderText: TextView = view.findViewById(R.id.txt_recordatorio)
        private val btnDeleteReminder: ImageButton = view.findViewById(R.id.btn_deleteReminder)
        private lateinit var context : Context

        fun bind(reminder: Reminder) {
            context = this.itemView.context
            txtReminderText.text = reminder.Text
            btnDeleteReminder.setOnClickListener{
                deleteReminder(reminder.ReminderId)
            }
        }

        /**
         * Quitar el recordatorio de la tarea y borrarlo de la base de datos,
         * después recargar el recycler de recordatorios en los detalles de la tarea
         * para mostar las modificaciones
         */
        private fun deleteReminder(reminderId: Long) {
            //TODO: deleteReminder()
            Log.i("reminder", "deleteReminder: id: " + reminderId)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = reminderList.size
}