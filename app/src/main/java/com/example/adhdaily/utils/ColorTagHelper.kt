package com.example.adhdaily.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.adhdaily.R
import com.example.adhdaily.model.database.LocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ColorTagHelper {

    /**
     * A partir del id de un colortag, obtener el filename del drawable a pintar
     * y settear el background del item del (recycler de DayView) al que corresponde
     */
    fun setColorTagInTaskItemBg(colorTagId: Long, context: Context, container: LinearLayout) {
        GlobalScope.launch(Dispatchers.IO) {
            val localDB = LocalDatabase.getInstance(context)
            val colorTag = localDB.colorTagsTaskDao().selectColorTagById(colorTagId)
            val filename = colorTag.Filename

            (context as Activity).runOnUiThread {
                if (filename != null) {
                    val resourceId =
                        context.resources.getIdentifier(filename, "drawable", context.packageName)
                    if (resourceId != 0) {
                        //si el recurso existe
                        container.setBackgroundResource(resourceId)
                    } else {
                        //si recurso no existe
                        container.setBackgroundResource(R.color.transparent)
                    }
                } else {
                    //no hay colorTagAsignado
                    container.setBackgroundResource(R.color.transparent)
                }
            }
        }
    }


    /**
     * Obtener el color en hexadecimal a partir del id del ColorTag
     * y pintarlo en un ImageView (usado en NewTaskFragment)
     */
    fun setColorToImageViewFromColorTagId(colorTagId: Long, context: Context, imageView: ImageView){
        GlobalScope.launch(Dispatchers.IO) {
            val localDB = LocalDatabase.getInstance(context)
            val colorTag = localDB.colorTagsTaskDao().selectColorTagById(colorTagId)

            if(colorTag != null) {
                val tint = Color.parseColor(colorTag.ColorCode)
                imageView.setColorFilter(tint)
            } else {
                //no hay colorTagAsignado
                imageView.setColorFilter(R.color.transparent)
            }
        }
    }

    /**
     * Obtener el nombre a partir del id del ColorTag
     * y escribirlo en un TextView (usado en NewTaskFragment)
     */
    fun setTextToTextViewFromColorTagId(colorTagId: Long, context: Context, textView: TextView){
        Log.i("radio", "setTextToTextViewFromColorTagId: " + colorTagId)
        val nameColorTag = when (colorTagId.toInt()) {
            1 -> R.string.lbl_colorTagNone
            2 -> R.string.lbl_colorTagBlue
            3 -> R.string.lbl_colorTagGreen
            4 -> R.string.lbl_colorTagPurple
            5 -> R.string.lbl_colorTagYellow
            6 -> R.string.lbl_colorTagRed

            else -> R.string.lbl_colorTagNone // Valor predeterminado
        }
        textView.setText(nameColorTag)

    }
}