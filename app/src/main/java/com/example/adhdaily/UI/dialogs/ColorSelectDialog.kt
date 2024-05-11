package com.example.adhdaily.UI.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.adhdaily.R
import com.example.adhdaily.UI.fragments.newTask.NewTaskFragment
import com.example.adhdaily.utils.ColorTagHelper

class ColorSelectDialog(context: Context, private val newTaskFragment: NewTaskFragment?, private val taskDetailsDialog: TaskDetailsDialog?) : Dialog(context) {

    private var btnSelectColor: Button
    private var radioGroupColores: RadioGroup

    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_color_select)

        //Configurar binding elementos dialog
        btnSelectColor = findViewById<Button>(R.id.btn_selectColor)
        btnSelectColor.setOnClickListener {
            selectColorTagForNewTask()
        }

        radioGroupColores =  findViewById<RadioGroup>(R.id.radioGroup)

    }

    /***
     * Para seleccionar un color tag al CREAR una tarea
     */
    private fun selectColorTagForNewTask() {
        val selectedRadioButtonId = radioGroupColores.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

        //obtenemos el número al final del id del radiobutton para saber a qué colorTagId corresponde
        val selectedColorTagId = selectedRadioButton.toString().split("_").last()[0]
        newTaskFragment!!.colorTagId = selectedColorTagId.digitToInt().toLong()

        //actualizar nombre del color seleccionado en el fragment
        val helper = ColorTagHelper()
        helper.setTextToTextViewFromColorTagId(newTaskFragment.colorTagId , context, newTaskFragment.txtColorTagName)
        //actualizar color del circulito según el colorTag seleccionado
        helper.setColorToImageViewFromColorTagId(newTaskFragment.colorTagId , context, newTaskFragment.imgvwColorTagIcon)

        this.dismiss()
    }

    /***
     * Para seleccionar un color tag al EDITAR una tarea
     */
    private fun selectColorTagForEditTask() {
        val selectedRadioButtonId = radioGroupColores.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

        //obtenemos el número al final del id del radiobutton para saber a qué colorTagId corresponde
        val selectedColorTagId = selectedRadioButton.toString().split("_").last()[0]
        taskDetailsDialog!!.colorTagId = selectedColorTagId.digitToInt().toLong()
        Log.i("RadioButton", "selectColorTagId: " + selectedColorTagId)

        //actualizar nombre del color seleccionado en el fragment
        val helper = ColorTagHelper()
        helper.setTextToTextViewFromColorTagId(taskDetailsDialog.colorTagId , context, taskDetailsDialog.txtColorTagName)

        //actualizar color del circulito según el colorTag seleccionado
        helper.setColorToImageViewFromColorTagId(taskDetailsDialog.colorTagId , context, taskDetailsDialog.imgvwColorTagIcon)

        this.dismiss()
    }


}