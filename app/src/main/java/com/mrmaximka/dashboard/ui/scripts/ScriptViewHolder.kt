package com.mrmaximka.dashboard.ui.scripts

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.ScriptList
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_script.view.*

class ScriptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), PathInterface {

    private lateinit var model: ScriptList
    private lateinit var context: Context
    private val dbTable: DbTable = DbTable()
    private lateinit var database: SQLiteDatabase
    private val listGoods: ArrayList<String> = ArrayList()
    private val listVideos: ArrayList<String> = ArrayList()
    private lateinit var update: UpdateInterface
    private var pathVideo: String = ""

    fun bind(
        model: ScriptList,
        context: Context?,
        updateInterface: UpdateInterface
    ) {
        this.model = model
        this.context = context!!
        this.update = updateInterface
        database = DbHelper(context).writableDatabase

        itemView.good1_name.text = model.good1
        itemView.good2_name.text = model.good2
        itemView.video_name.text = model.video

        itemView.btn_goods_edit.setOnClickListener { editScript() }
        itemView.btn_goods_del.setOnClickListener { delItem() }
        itemView.video_add_btn.setOnClickListener { addScript() }
        if (model.idScript == null){
            editScript()
        }
    }

    private fun addScript() {
        update.getVideo(this)

    }

    private fun editScript() {
        val database = DbHelper(context).writableDatabase

        val goods: HashMap<Int, String> = dbTable.getSensorsGoods(database)
        val videos: HashMap<Int, String> = dbTable.getScriptVideos(database)

        for (value in goods.values) {
            listGoods.add(value)
        }

        for (value in videos.values) {
            listVideos.add(value)
        }

        val goodsAdapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.simple_spinner_item, listGoods)
        goodsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        val videoAdapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.simple_spinner_item, listVideos)
        videoAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        showEditMenu()
        itemView.good1_name_list.adapter = goodsAdapter
        itemView.good2_name_list.adapter = goodsAdapter
        itemView.video_name_list.adapter = videoAdapter

        itemView.good1_name_list.prompt = "Выберите товар"
        itemView.good2_name_list.prompt = "Выберите товар"
        itemView.video_name_list.prompt = "Выберите видео"

        for (i in 0 until listGoods.size){
            if (listGoods[i] == itemView.good1_name.text){
                itemView.good1_name_list.setSelection(i)
                break
            }
        }

        for (i in 0 until listGoods.size){
            if (listGoods[i] == itemView.good2_name.text){
                itemView.good2_name_list.setSelection(i)
                break
            }
        }

        for (i in 0 until listVideos.size){
            if (listVideos[i] == itemView.video_name.text){
                itemView.video_name_list.setSelection(i)
                break
            }
        }

        itemView.btn_goods_ok.setOnClickListener { saveScript(goods, videos) }
    }

    @SuppressLint("CheckResult")
    private fun saveScript(
        goods: HashMap<Int, String>,
        videos: HashMap<Int, String>
    ) {
        val good = listGoods[itemView.good1_name_list.selectedItemPosition]
        val good2 = listGoods[itemView.good2_name_list.selectedItemPosition]

        var idGood1: Int? = null
        var idGood2: Int? = null
        var idVideo: Int? = null

        for ((key, value) in goods.entries) {
            if (value == good){
                idGood1 = key
            }
            if (value == good2){
                idGood2 = key
            }
        }

        if (model.idScript == null && pathVideo.isNotEmpty()){
            val video = itemView.video_name.text
            val videos2 = dbTable.getScriptVideos(database)
            for ((key, value) in videos2.entries) {
                if (value == video){
                    idVideo = key
                }
            }
        }
        else if (model.idScript != null && pathVideo.isNotEmpty()){
            val video = itemView.video_name.text
            val videos2 = dbTable.getScriptVideos(database)
            for ((key, value) in videos2.entries) {
                if (value == video){
                    idVideo = key
                }
            }
        }
        /*else if (model.idScript == null && pathVideo.isEmpty()){
            idVideo = 0
        }*/
        else{
            val video = listVideos[itemView.video_name_list.selectedItemPosition]
            for ((key, value) in videos.entries) {
                if (value == video){
                    idVideo = key
                }
            }
        }

        val editScriptRn: Runnable
        editScriptRn = if (model.idScript == null){
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.addScript(database, idGood1, idGood2, idVideo)
            }
        }else{
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.editScript(database, idGood1, idGood2, model.idScript, idVideo)
            }
        }

        Completable.fromRunnable(editScriptRn)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveItemObserver)
    }

    private fun closeEditMenu() {
        itemView.good1_name.visibility = View.VISIBLE
        itemView.good2_name.visibility = View.VISIBLE
        itemView.video_name.visibility = View.VISIBLE
        itemView.btn_goods_edit.visibility = View.VISIBLE
        itemView.btn_goods_del.visibility = View.VISIBLE
        itemView.good1_name_list.visibility = View.GONE
        itemView.good2_name_list.visibility = View.GONE
        itemView.video_add_btn.visibility = View.GONE
        itemView.video_name_list.visibility = View.GONE
        itemView.btn_goods_ok.visibility = View.GONE
    }

    private fun showEditMenu() {
        itemView.good1_name.visibility = View.GONE
        itemView.good2_name.visibility = View.GONE
        itemView.video_name.visibility = View.GONE
        itemView.btn_goods_edit.visibility = View.GONE
        itemView.btn_goods_del.visibility = View.GONE
        itemView.video_add_btn.visibility = View.GONE
        itemView.good1_name_list.visibility = View.VISIBLE
        itemView.good2_name_list.visibility = View.VISIBLE
        itemView.video_add_btn.visibility = View.VISIBLE
        itemView.video_name_list.visibility = View.VISIBLE
        itemView.btn_goods_ok.visibility = View.VISIBLE
    }

    @SuppressLint("CheckResult")
    private fun delItem() {
        val loadBrands = Runnable {
            Log.d("MMV", "Loading")
            dbTable.delScript(database, model.idScript)
        }

        Completable.fromRunnable(loadBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveItemObserver)
    }

    override fun setPath(path: String) {
        pathVideo = path
        val listPath = path.split("/")
        val fullName = listPath[listPath.size - 1]
        val nameIndex = fullName.lastIndexOf(".")
        val name = fullName.substring(0, nameIndex-1)

        dbTable.addVideo(database, path, name)

        itemView.video_name.text = name
        itemView.video_add_btn.visibility = View.GONE
        itemView.video_name_list.visibility = View.GONE
        itemView.video_name.visibility = View.VISIBLE
    }

    private val saveItemObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            closeEditMenu()
            update.update()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при удалении элемента", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}