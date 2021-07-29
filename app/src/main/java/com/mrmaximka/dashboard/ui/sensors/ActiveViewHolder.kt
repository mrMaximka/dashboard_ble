package com.mrmaximka.dashboard.ui.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.ActiveSensors
import com.mrmaximka.dashboard.model.GoodsList
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_active_sensor.view.*

class ActiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: ActiveSensors
    private var oldArcticul: String = ""
    private lateinit var context: Context
    private val dbTable: DbTable = DbTable()
    private lateinit var database: SQLiteDatabase
    private var update: ActiveAdapter.UpdateFragment? = null

    fun bind(
        activeSensors: ActiveSensors,
        context: Context?,
        update: ActiveAdapter.UpdateFragment
    ) {
        this.model = activeSensors
        this.context = context!!
        this.oldArcticul = model.articul
        this.update = update
        database = DbHelper(context).writableDatabase
        itemView.active_serial.text = model.serial
        if (model.name == "0"){
            itemView.active_name.text = "Нет товара"
        }
        else{
            itemView.active_name.text = model.name
        }
        itemView.active_articul.text = model.articul

        itemView.btn_scan_edit.setOnClickListener { editItem() }
        itemView.btn_scan_del.setOnClickListener { delItem() }
    }

    @SuppressLint("CheckResult")
    private fun delItem() {
        val delActiveRun = Runnable {
            Log.d("MMV", "Loading")
            dbTable.delSensor(database, model.idSens)
        }

        Completable.fromRunnable(delActiveRun)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveItemObserver)
    }

    @SuppressLint("CheckResult")
    private fun editItem() {
        val database = DbHelper(context).writableDatabase

        val goodsList: GoodsList = dbTable.getGoodsList(database)
        val goods: HashMap<String, String> = goodsList.map
        val goodsId: HashMap<String, Int> = goodsList.mapId
        var idGood: Int? = null

        val list: ArrayList<String> = ArrayList()
        for (key in goods.keys) {
            list.add(key)
        }

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)

        openEditMenu()
        itemView.active_name_list.adapter = arrayAdapter

        itemView.active_name_list.prompt = "Выберите товар"

        var index: Int? = null
        for (i in 0 until list.size){
            if (list[i] == itemView.active_name.text){
                index = i
                break
            }
        }

        if (index != null){
            itemView.active_name_list.setSelection(index)
        }

        itemView.active_name_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                itemView.active_articul.text = goods[list[p2]]
                idGood = goodsId[list[p2]]
            }

        }

        itemView.btn_scan_ok.setOnClickListener {
            val addActiveRun: Runnable
            if (idGood != null){
//                itemView.active_name.text = itemView.active_name_list.selectedItem.toString()
//                oldArcticul = itemView.active_articul.text as String
                addActiveRun = Runnable {
                    Log.d("MMV", "Loading")
                    dbTable.editSensor(database, model.idSens, idGood)
                }
            }
                else{
//                itemView.active_name.text = "Нет товара"
//                oldArcticul = "null"
                addActiveRun = Runnable {
                    Log.d("MMV", "Loading")
                    dbTable.editSensor(database, model.idSens, 0)
                }
            }

            Completable.fromRunnable(addActiveRun)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(saveItemObserver)
        }
    }

    private fun openEditMenu(){
        itemView.active_name.visibility = View.GONE
        itemView.btn_scan_edit.visibility = View.GONE
        itemView.btn_scan_del.visibility = View.GONE
        itemView.btn_scan_ok.visibility = View.VISIBLE
        itemView.active_name_list.visibility = View.VISIBLE
    }

    private fun closeEditMenu(){
        itemView.active_name.visibility = View.VISIBLE
        itemView.btn_scan_edit.visibility = View.VISIBLE
        itemView.btn_scan_del.visibility = View.VISIBLE
        itemView.btn_scan_ok.visibility = View.GONE
        itemView.active_name_list.visibility = View.GONE
    }

    private val saveItemObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            closeEditMenu()
            update?.updateFragmentActive()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при изменении элемента", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}