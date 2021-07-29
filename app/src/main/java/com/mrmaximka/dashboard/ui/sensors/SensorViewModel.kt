package com.mrmaximka.dashboard.ui.sensors

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.ActiveSensors
import com.mrmaximka.dashboard.model.ScanSensors

class SensorViewModel : ViewModel() {

    private val dbTable: DbTable = DbTable()


    fun loadActive(context: Context?): ArrayList<ActiveSensors> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadActive(database)
    }

    fun loadScan(context: Context?): ArrayList<ScanSensors> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadScan(database)
    }
}