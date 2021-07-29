package com.mrmaximka.dashboard.ui.products.lists

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
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.model.ProductModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_product.view.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var model: ProductModel
    private lateinit var context: Context
    private val dbTable: DbTable = DbTable()
    private lateinit var database: SQLiteDatabase
    private lateinit var updateInterface: UpdateInterface
    private lateinit var categoryList: ArrayList<CategoryModel>
    private lateinit var allBrandList: ArrayList<BrandModel>

    fun bind(
        model: ProductModel,
        context: Context?,
        updateInterface: UpdateInterface,
        categoryList: ArrayList<CategoryModel>,
        allBrandList: ArrayList<BrandModel>
    ) {
        this.model = model
        this.context = context!!
        this.updateInterface = updateInterface
        this.categoryList = categoryList
        this.allBrandList = allBrandList
        database = DbHelper(context).writableDatabase

        itemView.item_product_name.text = model.name
        itemView.item_product_articul.text = model.articul
        itemView.item_product_brand.text = model.brand
        itemView.item_product_cat.text = model.category

        itemView.btn_product_del.setOnClickListener { delProduct() }
        itemView.btn_product_edit.setOnClickListener { editProduct() }
        itemView.btn_product_ok.setOnClickListener { saveProduct() }

        if (model.idGood == null){
            editProduct()
        }
    }

    @SuppressLint("CheckResult")
    private fun saveProduct() {
        if (itemView.item_product_name_et.text.toString().isEmpty()){
            Toast.makeText(context, "Название товара не может быть пустым", Toast.LENGTH_LONG).show()
            return
        }

        if (itemView.item_product_articul_et.text.toString().isEmpty()){
            Toast.makeText(context, "Артикул товара не может быть пустым", Toast.LENGTH_LONG).show()
            return
        }

        val name = itemView.item_product_name_et.text.toString()
        val articul = itemView.item_product_articul_et.text.toString()
        val idCat: Int = if (itemView.item_product_cat_list.selectedItemPosition == 0){
            0
        }else{
            categoryList[itemView.item_product_cat_list.selectedItemPosition-1].idCategory!!
        }

        val idBrand: Int = if(itemView.item_product_brand_list.selectedItemPosition == 0){
            0
        }else{
            allBrandList[itemView.item_product_brand_list.selectedItemPosition-1].idBrand!!
        }

        val saveProd: Runnable
        saveProd = if (model.idGood == null){
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.addProduct(database, name, articul, idCat, idBrand)
            }
        }else{
            Runnable {
                Log.d("MMV", "Loading")
                dbTable.editProduct(database, model.idGood!!, name, articul, idCat, idBrand)
            }
        }

        Completable.fromRunnable(saveProd)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveProductObserver)
    }

    private fun editProduct() {
        if (model.idGood != null){
            itemView.item_product_name_et.setText(itemView.item_product_name.text)
            itemView.item_product_articul_et.setText(itemView.item_product_articul.text)
        }

        val listBrand: ArrayList<String> = ArrayList()
        val listCat: ArrayList<String> = ArrayList()

        listBrand.add("Нет бренда")
        listCat.add("Нет категории")
        for (brand in 0 until allBrandList.size){
            listBrand.add(allBrandList[brand].name)
        }
        for (cat in 0 until categoryList.size){
            listCat.add(categoryList[cat].name)
        }

        val brandAdapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.simple_spinner_item, listBrand)
        brandAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        val catAdapter: ArrayAdapter<String> = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, listCat)
        catAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        itemView.item_product_brand_list.adapter = brandAdapter
        itemView.item_product_cat_list.adapter = catAdapter

        for (brand in 0 until listBrand.size){
            if (listBrand[brand] == itemView.item_product_brand.text){
                itemView.item_product_brand_list.setSelection(brand)
            }
        }

        for (cat in 0 until listCat.size){
            if (listCat[cat] == itemView.item_product_cat.text){
                itemView.item_product_cat_list.setSelection(cat)
            }
        }

        itemView.item_product_brand_list.prompt = "Выберите бренд"
        itemView.item_product_cat_list.prompt = "Выберите категорию"

        showEditMenu()
    }

    @SuppressLint("CheckResult")
    private fun delProduct() {
        val delProduct = Runnable {
            Log.d("MMV", "Loading")
            dbTable.delProduct(database, model.idGood)
        }

        Completable.fromRunnable(delProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(saveProductObserver)
    }

    private fun showEditMenu() {
        itemView.item_product_name.visibility = View.GONE
        itemView.item_product_articul.visibility = View.GONE
        itemView.item_product_cat.visibility = View.GONE
        itemView.item_product_brand.visibility = View.GONE
        itemView.btn_product_edit.visibility = View.GONE
        itemView.btn_product_del.visibility = View.GONE
        itemView.item_product_cat_list.visibility = View.VISIBLE
        itemView.item_product_articul_et_container.visibility = View.VISIBLE
        itemView.item_product_brand_list.visibility = View.VISIBLE
        itemView.item_product_name_et_container.visibility = View.VISIBLE
        itemView.btn_product_ok.visibility = View.VISIBLE
    }

    private fun closeEditMenu() {
        itemView.item_product_name.visibility = View.VISIBLE
        itemView.item_product_articul.visibility = View.VISIBLE
        itemView.item_product_cat.visibility = View.VISIBLE
        itemView.item_product_brand.visibility = View.VISIBLE
        itemView.btn_product_edit.visibility = View.VISIBLE
        itemView.btn_product_del.visibility = View.VISIBLE
        itemView.item_product_cat_list.visibility = View.GONE
        itemView.item_product_articul_et_container.visibility = View.GONE
        itemView.item_product_brand_list.visibility = View.GONE
        itemView.item_product_name_et_container.visibility = View.GONE
        itemView.btn_product_ok.visibility = View.GONE
    }

    private val saveProductObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            closeEditMenu()
            updateInterface.updateProducts()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при удалении/изменении товара", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}