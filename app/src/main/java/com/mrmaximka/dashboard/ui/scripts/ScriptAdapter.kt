package com.mrmaximka.dashboard.ui.scripts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.ScriptList

class ScriptAdapter(val context: Context?, private val updateInterface: UpdateInterface) : RecyclerView.Adapter<ScriptViewHolder>() {

    private var scripts: List<ScriptList> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_script, parent, false)
        return ScriptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  scripts.size
    }

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        holder.bind(scripts[position], context, updateInterface)
    }

    fun setElements(list: ArrayList<ScriptList>) {
        this.scripts = list
        notifyDataSetChanged()
    }

    fun addNewElement() {
        if (scripts[scripts.size - 1].idScript == null){
            return
        }
        val scriptList = ScriptList()
        scripts = scripts + scriptList
        notifyItemInserted(scripts.size - 1)
    }
}