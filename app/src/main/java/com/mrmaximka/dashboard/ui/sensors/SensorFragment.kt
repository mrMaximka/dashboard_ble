package com.mrmaximka.dashboard.ui.sensors

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.ActiveSensors
import com.mrmaximka.dashboard.model.ScanSensors
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sensor.*

class SensorFragment : Fragment(), ScanAdapter.UpdateFragment, ActiveAdapter.UpdateFragment {

    private lateinit var viewModel: SensorViewModel
    private lateinit var activeList: RecyclerView
    private lateinit var scanList: RecyclerView

    private lateinit var activeAdapter: ActiveAdapter
    private lateinit var scanAdapter: ScanAdapter

    private var allActiveList: ArrayList<ActiveSensors> = ArrayList()
    private var allScanList: ArrayList<ScanSensors> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sensor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SensorViewModel::class.java)

        Handler().postDelayed({
            initView()
            setViewSettings()
            loadActive()
            loadScan()
        }, 200)
    }

    @SuppressLint("CheckResult")
    private fun loadScan() {
        val loadActive = Runnable {
            Log.d("MMV", "Loading")
            allActiveList = viewModel.loadActive(context)
        }

        Completable.fromRunnable(loadActive)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(scanObserver)
    }

    @SuppressLint("CheckResult")
    private fun loadActive() {
        val loadScan = Runnable {
            Log.d("MMV", "Loading")
            allScanList = viewModel.loadScan(context)
        }

        Completable.fromRunnable(loadScan)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(activeObserver)

    }

    private fun setViewSettings() {
        activeList.layoutManager = LinearLayoutManager(context)
        scanList.layoutManager = LinearLayoutManager(context)

        activeAdapter = ActiveAdapter(context, this)
        activeList.adapter = activeAdapter

        scanAdapter = ScanAdapter(context, this)
        scanList.adapter = scanAdapter
    }

    private fun initView() {
        activeList = view!!.findViewById(R.id.active_sensors_list)
        scanList = view!!.findViewById(R.id.scan_sensors_list)
    }

    override fun updateFragment() {
        pbActive.visibility = View.VISIBLE
        active_sensors_list.visibility = View.GONE
        loadScan()
    }

    override fun updateFragmentActive() {
        pbActive.visibility = View.VISIBLE
        active_sensors_list.visibility = View.GONE
        loadScan()
    }

    private val scanObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            activeAdapter.setElements(allActiveList)
            pbActive.visibility = View.GONE
            active_sensors_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке активных сенсоров", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    private val activeObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            scanAdapter.setElements(allScanList)
            pbScan.visibility = View.GONE
            scan_sensors_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке сканера сенсоров", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}