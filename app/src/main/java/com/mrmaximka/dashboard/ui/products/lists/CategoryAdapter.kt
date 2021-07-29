package com.mrmaximka.dashboard.ui.products.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.CategoryModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface

class CategoryAdapter(val context: Context?, private val updateInterface: UpdateInterface) : RecyclerView.Adapter<CategoryViewHolder>() {

    private var categories: List<CategoryModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], context, updateInterface)
    }

    fun setElements(list: ArrayList<CategoryModel>) {
        this.categories = list
        notifyDataSetChanged()
    }

    fun addNewElement() {
        val categoryModel = CategoryModel()
        categories = categories + categoryModel
        notifyDataSetChanged()
    }
}