package com.mrmaximka.dashboard.ui.products.lists

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_brand.view.*

class BrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: BrandModel
    private lateinit var context: Context
    private val dbTable: DbTable = DbTable()
    private lateinit var database: SQLiteDatabase
    private lateinit var updateInterface: UpdateInterface

    fun bind(
        model: BrandModel,
        context: Context?,
        updateInterface: UpdateInterface
    ) {
        this.model = model
        this.context = context!!
        this.updateInterface = updateInterface
        database = DbHelper(context).writableDatabase
        itemView.item_brand_name.text = model.name

        itemView.btn_item_brand_del.setOnClickListener { delBrand() }
        itemView.btn_item_brand_edit.setOnClickListener { editBrand() }
        itemView.btn_item_brand_ok.setOnClickListener { saveBrand() }

        if (model.idBrand == null){
            editBrand()
        }
    }

    @SuppressLint("CheckResult")
    private fun delBrand() {
        val loadBrands = Runnable {
            Log.d("MMV", "Loading")
            dbTable.delBrand(database, model.idBrand)
        }

        Completable.fromRunnable(loadBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveBrandObserver)
    }

    private fun editBrand() {
        if (model.name.isNotEmpty()){
            itemView.item_brand_et.setText(model.name)
        }
        itemView.item_brand_et.isFocusable = true
        showEditMenu()
    }

    private fun showEditMenu() {
        itemView.item_brand_name.visibility = View.GONE
        itemView.btn_item_brand_edit.visibility = View.GONE
        itemView.btn_item_brand_del.visibility = View.GONE
        itemView.item_brand_et_container.visibility = View.VISIBLE
        itemView.btn_item_brand_ok.visibility = View.VISIBLE
    }

    @SuppressLint("CheckResult")
    private fun saveBrand() {
        val newName: String = itemView.item_brand_et.text.toString()
        if (newName.isEmpty()){
            Toast.makeText(context, "Название бренда не может быть пустым", Toast.LENGTH_LONG).show()
            return
        }
        val saveBrands: Runnable
        saveBrands = if (model.idBrand == null){
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.addBrand(database, newName)
            }
        }else{
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.editBrand(database, newName, model.idBrand)
            }
        }

        Completable.fromRunnable(saveBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveBrandObserver)
    }

    private fun closeEditMenu() {
        itemView.item_brand_name.visibility = View.VISIBLE
        itemView.btn_item_brand_edit.visibility = View.VISIBLE
        itemView.btn_item_brand_del.visibility = View.VISIBLE
        itemView.item_brand_et_container.visibility = View.GONE
        itemView.btn_item_brand_ok.visibility = View.GONE
    }

    private val saveBrandObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            closeEditMenu()
            updateInterface.updateBrands()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при удалении/изменении бренда", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}