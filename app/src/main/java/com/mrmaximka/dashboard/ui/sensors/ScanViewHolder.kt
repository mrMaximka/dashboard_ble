package com.mrmaximka.dashboard.ui.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.ScanSensors
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_scan_sensor.view.*


class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: ScanSensors
    private val dbTable: DbTable = DbTable()
    private lateinit var context: Context
    private var update: ScanAdapter.UpdateFragment? = null

    fun bind(
        scanSensors: ScanSensors,
        context: Context?,
        update: ScanAdapter.UpdateFragment
    ) {
        this.model = scanSensors
        this.context = context!!
        this.update = update
        itemView.scan_name.text = model.name
        itemView.scan_date.text = model.date

        itemView.btn_scan_add.setOnClickListener { delItem() }
    }

    @SuppressLint("CheckResult")
    private fun delItem() {

        val delScanRun = Runnable {
            Log.d("MMV", "Loading")
            val database = DbHelper(context).writableDatabase
            dbTable.addSensor(database, model.name)
        }

        Completable.fromRunnable(delScanRun)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(delItemObserver)
    }

    private val delItemObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            update?.updateFragment()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при удалении элемента", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

}