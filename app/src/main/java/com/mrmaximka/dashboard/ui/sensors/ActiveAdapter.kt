package com.mrmaximka.dashboard.ui.sensors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.ActiveSensors

class ActiveAdapter(val context: Context?, var update: UpdateFragment) : RecyclerView.Adapter<ActiveViewHolder>() {

    private var sensors: List<ActiveSensors> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_active_sensor, parent, false)
        return ActiveViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sensors.size
    }

    override fun onBindViewHolder(holder: ActiveViewHolder, position: Int) {
        holder.bind(sensors[position], context, update)
    }

    fun setElements(sensors: List<ActiveSensors>) {
        this.sensors = sensors
        notifyDataSetChanged()
    }

    interface UpdateFragment{
        fun updateFragmentActive()
    }
}