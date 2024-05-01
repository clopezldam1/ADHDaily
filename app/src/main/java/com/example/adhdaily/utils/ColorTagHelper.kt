package com.example.adhdaily.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.LinearLayout
import com.example.adhdaily.R
import com.example.adhdaily.model.DAO.ColorTagsTaskDAO
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

            if(filename != null) {
                val resourceId =  context.resources.getIdentifier(filename, "drawable", context.packageName)
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


    /**
     * Obtener el color a pintar a partir del id del ColorTag
     *
     * @return color en hexadecimal del colorTag
     */
    fun getColorTagColorFromId(colorTagId: Long, context: Context): String{
        //GlobalScope.launch(Dispatchers.IO) {
        val localDB = LocalDatabase.getInstance(context)
        val colorTag = localDB.colorTagsTaskDao().selectColorTagById(colorTagId)
        //}

        return colorTag.ColorCode //nombre archivo sin extension
    }
}