package com.mrmaximka.dashboard.ui.products.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.BrandModel
import com.mrmaximka.dashboard.ui.products.UpdateInterface

class BrandAdapter(val context: Context?, private val updateInterface: UpdateInterface) : RecyclerView.Adapter<BrandViewHolder>() {

    private var brands: List<BrandModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun getItemCount(): Int {
        return brands.size
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(brands[position], context, updateInterface)
    }

    fun setElements(list: ArrayList<BrandModel>) {
        this.brands = list
        notifyDataSetChanged()
    }

    fun addNewElement() {
        val brandModel = BrandModel()
        brands = brands + brandModel
        notifyDataSetChanged()
    }
}