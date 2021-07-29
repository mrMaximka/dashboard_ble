package com.mrmaximka.dashboard.ui.scripts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.ScriptList

class ScriptViewModel : ViewModel() {

    private val dbTable: DbTable = DbTable()


    fun loadScripts(context: Context?): ArrayList<ScriptList> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadScripts(database)
    }

    fun addScript(adapter: ScriptAdapter) {
        adapter.addNewElement()
    }
}