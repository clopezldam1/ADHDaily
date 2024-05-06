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

class ColorSelectDialog(context: Context, private val newTaskFragment: NewTaskFragment) : Dialog(context) {

    private var btnSelectColor: Button
    private var radioGroupColores: RadioGroup

    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_color_select)

        //Configurar binding elementos dialog
        btnSelectColor = findViewById<Button>(R.id.btn_selectColor)
        btnSelectColor.setOnClickListener {
            selectColorTag()
        }

        radioGroupColores =  findViewById<RadioGroup>(R.id.radioGroup)

    }

    //TODO: selectColorTag() -> meter el id del colortag en la variable
    private fun selectColorTag() {
        val selectedRadioButtonId = radioGroupColores.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

        //obtenemos el número al final del id del radiobutton para saber a qué colorTagId corresponde
        val selectedColorTagId = selectedRadioButton.toString().split("_").last()[0]
        newTaskFragment.colorTagId = selectedColorTagId.digitToInt().toLong()
        Log.i("RadioButton", "selectColorTagId: " + selectedColorTagId)

        //Ahora cambiamos el nombre del color seleccionado en el fragment
        val helper = ColorTagHelper()
        helper.setTextToTextViewFromColorTagId(newTaskFragment.colorTagId , context, newTaskFragment.txtColorTagName)

        //Ahora cambiamos el color del circulito según el colorTag seleccionado
        helper.setColorToImageViewFromColorTagId(newTaskFragment.colorTagId , context, newTaskFragment.imgvwColorTagIcon)

        this.dismiss()
    }


}