package com.mrmaximka.dashboard.ui.products.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.model.ProductModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface

class ProductAdapter(val context: Context?, private val updateInterface: UpdateInterface) : RecyclerView.Adapter<ProductViewHolder>() {

    private var products: List<ProductModel> = emptyList()
    private lateinit var categoryList: ArrayList<CategoryModel>
    private lateinit var allBrandList: ArrayList<BrandModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], context, updateInterface, categoryList, allBrandList)
    }

    fun setElements(
        list: ArrayList<ProductModel>,
        categoryList: ArrayList<CategoryModel>,
        allBrandList: ArrayList<BrandModel>
    ) {
        this.products = list
        this.categoryList = categoryList
        this.allBrandList = allBrandList
        notifyDataSetChanged()
    }

    fun addNewElement() {
        val productModel = ProductModel()
        products = products + productModel
        notifyDataSetChanged()
    }
}