package com.mrmaximka.dashboard.ui.stand

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.StandSettings

class StandSettingsViewModel : ViewModel() {

    private val dbTable: DbTable = DbTable()

    var saveStandClick = MutableLiveData<Boolean>().apply {
        value = false
    }

    var saveMoveClick = MutableLiveData<Boolean>().apply {
        value = false
    }


    fun onSaveStandClick(){
        saveStandClick.value = true
    }

    fun onSaveMoveClick(){
        saveMoveClick.value = true
    }

    fun saveStand(
        context: Context?,
        name: Editable,
        timezone: Editable,
        description: Editable
    ) {
        val database = DbHelper(context!!).writableDatabase
        dbTable.updateStand(database, name.toString(), timezone.toString(), description.toString())
    }

    fun saveMove(
        context: Context?,
        etStandVideoDelay: String,
        etStandWaitTime: String,
        cbStop: Boolean,
        cbReplay: Boolean,
        etStandPlayOne: String,
        etStandPlayTwo: String,
        cbBreakTwo: Boolean,
        etStandSensitivity: String
    ) {
        val database = DbHelper(context!!).writableDatabase
        dbTable.updateMove(
            database,
            etStandVideoDelay.toInt(),
            etStandWaitTime.toInt(),
            cbStop.toInt(),
            cbReplay.toInt(),
            etStandPlayOne.toInt(),
            etStandPlayTwo.toInt(),
            cbBreakTwo.toInt(),
            etStandSensitivity.toInt()
        )
    }

    fun loadStand(context: Context?): StandSettings {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadStand(database)
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}