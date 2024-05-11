package com.example.adhdaily.UI.recyclers

import android.content.Context
import android.graphics.Paint
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R
import com.example.adhdaily.UI.dialogs.ConfirmDeleteTaskDialog
import com.example.adhdaily.UI.dialogs.TaskDetailsDialog
import com.example.adhdaily.model.database.LocalDatabase
import com.example.adhdaily.model.entity.Task
import com.example.adhdaily.utils.ColorTagHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskListDayRecycler(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListDayRecycler.ViewHolder>() {



    /**
     * Inflamos la vista asociada al viewHolder (de cada item)
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_task_item, viewGroup, false)

        return ViewHolder(view)
    }

    /**
     * Asociar cada objeto de la taskList a nuestro viewHolder
     */
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(taskList.isNotEmpty()) {
            val task: Task = taskList[position]
            viewHolder.bind(task)
        }
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
        private lateinit var context : Context

        fun bind(task: Task) {
            context = this.itemView.context

            //truncate title if too long
            if (task.Title.length > 31) {
                txtTitle.text = task.Title.substring(0, 31 - 3) + "..."
            } else {
                txtTitle.text = task.Title
            }

            //binding y visibility del text de hora de inicio
            if (task.StartTime.equals("null")) {
                txtTimeStart.visibility = View.GONE
            } else {
                txtTimeStart.text = "[${task.StartTime}]"
                txtTimeStart.visibility = View.VISIBLE

                //truncate title if too long
                if (task.Title.length > 23) {
                    txtTitle.text = task.Title.substring(0, 23 - 3) + "..."
                } else {
                    txtTitle.text = task.Title
                }
                txtTitle.filters = arrayOf(InputFilter.LengthFilter(25)) //maxChars = 25 cuando se muestra la hora de inicio
            }

            //listener y binding del check
            checkCompleted.isChecked = task.Completed
            checkCompleted.setOnCheckedChangeListener{  check, isChecked ->
                if (isChecked) {
                    markCompleted()
                }else{
                    markPendant()
                }

                //UPDATE TASK.COMPLETED IN BD WHEN CHECK CHANGED
                GlobalScope.launch(Dispatchers.IO) {
                    task.Completed = isChecked
                    val localDB = LocalDatabase.getInstance(context)
                    localDB.taskDao().update(task)
                }
            }

            //estado incial de la tarea
            if (task.Completed) {
              markCompleted()
            }

            //cargar etiqueta de color
            if (task.ColorTag_FK.toInt() != 1) {
                val helper = ColorTagHelper()
                helper.setColorTagInTaskItemBg(task.ColorTag_FK, context,taskItemContainerListDayView)
            }

            //Onclick del contenedor para ampliar detalles de tarea
            txtTitle.setOnClickListener {
                openTaskDetails(task.TaskId)
            }

            //TODO: hacer que al mantener pulsado, puedas reordenar recyclerview --> future update

        }

        /**
         * Marcar tarea como completada
         */
        fun markCompleted(){
            txtTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //tachamos texto
            txtTimeStart.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG //tachamos texto
            taskItemContainerListDayView.alpha = 0.4f //le bajamos la opacidad al 40% al contenedor entero
        }


        /**
         * Volver a marcar tarea como pendiente
         */
        fun markPendant(){
            txtTitle.paintFlags = 0
            txtTimeStart.paintFlags = 0
            taskItemContainerListDayView.alpha = 1f
        }

        /**
         * Ver los detalles de una tarea al hacer click en ella
         */
        fun openTaskDetails(taskId: Long){
            val dialog = TaskDetailsDialog(context,taskId)
            dialog.show()

            dialog.setOnDismissListener {
                //Todo: when taskDetails dialog closes, must always reload recycler to show changes
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = taskList.size

}