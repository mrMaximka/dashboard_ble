package com.mrmaximka.dashboard.ui.products

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.model.ProductModel
import com.mrmaximka.dashboard.ui.products.lists.BrandAdapter
import com.mrmaximka.dashboard.ui.products.lists.CategoryAdapter
import com.mrmaximka.dashboard.ui.products.lists.ProductAdapter

class ProductViewModel : ViewModel() {

    private val dbTable: DbTable = DbTable()

    fun loadBrands(context: Context?): ArrayList<BrandModel> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadBrands(database)
    }

    fun loadCategories(context: Context?): ArrayList<CategoryModel> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadCategories(database)
    }

    fun loadProducts(context: Context?): ArrayList<ProductModel> {
        val database = DbHelper(context!!).writableDatabase
        return dbTable.loadProducts(database)
    }

    fun addBrand(adapter: BrandAdapter) {
        adapter.addNewElement()
    }

    fun addCategory(adapter: CategoryAdapter) {
        adapter.addNewElement()
    }

    fun addProduct(adapter: ProductAdapter) {
        adapter.addNewElement()
    }
}