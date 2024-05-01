package com.example.adhdaily.UI.recyclers

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.activities.MainActivity
import com.example.adhdaily.model.entity.Task

class TaskListDayRecycler(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListDayRecycler.ViewHolder>() {

    /**
     * Inflamos la vista asociada al viewHolder (de cada item)
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_task_item, viewGroup, false)

        return ViewHolder(view)
    }

    /**
     * Asociar cada objeto de la taskList a nuestro viewHolder
     */
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val task: Task = taskList[position]
        viewHolder.bind(task)
    }

    /**
     * Crear el viewHolder, donde asociamos los datos
     * del objeto a los elementos de la vista
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkCompleted: CheckBox = view.findViewById(R.id.checkbox_completeTask)
        private val txtTimeStart: TextView = view.findViewById(R.id.txt_timeStartTask)
        private val txtTitle: TextView = view.findViewById(R.id.txt_titleTask)
        private val taskItemContainerListDayView: LinearLayout = view.findViewById(R.id.taskItemContainerListDayView)

        fun bind(task: Task) {

            txtTitle.text = task.Title

            if (task.StartTime != null) {
                txtTimeStart.text = "[${task.StartTime}]"
                txtTimeStart.visibility = View.VISIBLE
            } else {
                txtTimeStart.visibility = View.GONE
            }

            //TODO: hacer logica para fondo de container según etiqueta color selected

            checkCompleted.isChecked = task.Completed
            if(task.Completed) {
                txtTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //tachamos texto
                txtTimeStart.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //tachamos texto
                taskItemContainerListDayView.alpha = 0.4f //le bajamos la opacidad al 40% al contenedor entero
            }




        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = taskList.size

}