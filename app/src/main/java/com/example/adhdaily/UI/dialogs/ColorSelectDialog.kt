package com.example.adhdaily.UI.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.adhdaily.R
import com.example.adhdaily.UI.fragments.newTask.NewTaskFragment

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
        Log.i("RadioButton", "Color Tag seleccionado: $selectedColorTagId")

        newTaskFragment.colorTagId = selectedColorTagId.digitToInt() //pasamos el char a int
        Log.i("RadioButton", "selectColorTag: EN FRAGMENT:" +  newTaskFragment.colorTagId )
        this.dismiss()
    }


}