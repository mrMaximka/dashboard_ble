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
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: CategoryModel
    private lateinit var context: Context
    private val dbTable: DbTable = DbTable()
    private lateinit var database: SQLiteDatabase
    private lateinit var updateInterface: UpdateInterface

    fun bind(
        model: CategoryModel,
        context: Context?,
        updateInterface: UpdateInterface
    ) {
        this.model = model
        this.context = context!!
        this.updateInterface = updateInterface
        database = DbHelper(context).writableDatabase
        itemView.item_cat_name.text = model.name

        itemView.btn_item_cat_del.setOnClickListener { delBrand() }
        itemView.btn_item_cat_edit.setOnClickListener { editBrand() }
        itemView.btn_item_cat_ok.setOnClickListener { saveBrand() }

        if (model.idCategory == null){
            editBrand()
        }
    }

    @SuppressLint("CheckResult")
    private fun delBrand() {
        val loadBrands = Runnable {
            Log.d("MMV", "Loading")
            dbTable.delCategory(database, model.idCategory)
        }
        Completable.fromRunnable(loadBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveCategoryObserver)
    }

    private fun editBrand() {
        if (model.name.isNotEmpty()){
            itemView.item_cat_et.setText(model.name)
        }
        itemView.item_cat_et.isFocusable = true
        showEditMenu()
    }

    @SuppressLint("CheckResult")
    private fun saveBrand() {
        val newName: String = itemView.item_cat_et.text.toString()
        if (newName.isEmpty()){
            Toast.makeText(context, "Название бренда не может быть пустым", Toast.LENGTH_LONG).show()
            return
        }

        val saveCategory: Runnable
        saveCategory = if (model.idCategory == null){
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.addCategory(database, newName)
            }
        }else{
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.editCategory(database, newName, model.idCategory)
            }
        }

        Completable.fromRunnable(saveCategory)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveCategoryObserver)
    }

    private fun showEditMenu() {
        itemView.item_cat_name.visibility = View.GONE
        itemView.btn_item_cat_edit.visibility = View.GONE
        itemView.btn_item_cat_del.visibility = View.GONE
        itemView.item_cat_et_container.visibility = View.VISIBLE
        itemView.btn_item_cat_ok.visibility = View.VISIBLE
    }

    private fun closeEditMenu() {
        itemView.item_cat_name.visibility = View.VISIBLE
        itemView.btn_item_cat_edit.visibility = View.VISIBLE
        itemView.btn_item_cat_del.visibility = View.VISIBLE
        itemView.item_cat_et_container.visibility = View.GONE
        itemView.btn_item_cat_ok.visibility = View.GONE
    }

    private val saveCategoryObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            closeEditMenu()
            updateInterface.updateCategories()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при удалении/изменении категории", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}