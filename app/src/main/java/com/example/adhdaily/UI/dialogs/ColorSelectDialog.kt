package com.example.adhdaily.UI.dialogs

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adhdaily.R

class ColorSelectDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {


    init {
        //Darle el layout al dialog
        setContentView(R.layout.dialog_color_select)

        //Configurar binding elementos dialog

    }


}