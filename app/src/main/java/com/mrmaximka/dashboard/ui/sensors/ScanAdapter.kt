package com.mrmaximka.dashboard.ui.sensors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.ScanSensors

class ScanAdapter(val context: Context?, var update: UpdateFragment) : RecyclerView.Adapter<ScanViewHolder>() {

    private var sensors: List<ScanSensors> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_scan_sensor, parent, false)
        return ScanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sensors.size
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.bind(sensors[position], context, update)
    }

    fun setElements(sensors: List<ScanSensors>) {
        this.sensors = sensors
        notifyDataSetChanged()
    }

    interface UpdateFragment{
        fun updateFragment()
    }
}