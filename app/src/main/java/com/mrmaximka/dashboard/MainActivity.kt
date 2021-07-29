package com.mrmaximka.dashboard

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.ui.AppBarConfiguration
import com.mrmaximka.dashboard.db.DbHelper
import com.mrmaximka.dashboard.db.DbTable
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val REQUEST_BT_PERMISSIONS: Int = 0
    private val REQUEST_BT_ENABLE: Int = 1

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navLogo: LinearLayout

    private lateinit var adapter: BluetoothAdapter
    private lateinit var scanner: BluetoothLeScanner
    private val dbTable: DbTable = DbTable()
    private var sensitivity: Int? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_stand, R.id.nav_sensor, R.id.nav_script,
                R.id.nav_product
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView: View = navView.getHeaderView(0)
        navLogo = headerView.findViewById(R.id.nav_logo)
        navLogo.setOnClickListener {
            navController.navigate(R.id.nav_stand)
        }

        setScanSettings()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setScanSettings() {
        adapter = BluetoothAdapter.getDefaultAdapter()
        scanner = adapter.bluetoothLeScanner

        getSensitivity()
        checkBtPermissions()
        enableBt()
        Toast.makeText(this, "Сканирование запущено", Toast.LENGTH_LONG).show()
        scanner.startScan(scanCallback)
    }

    private fun checkBtPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_BT_PERMISSIONS)
        }
    }

    private fun enableBt() {

        if (!adapter.isEnabled){
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_BT_ENABLE)
        }
    }

    @SuppressLint("CheckResult")
    private fun getSensitivity() {

        val loadSensitivity = Runnable {
            val database = DbHelper(this).writableDatabase
            sensitivity = dbTable.getSensitivity(database)
            database.close()
        }

        Completable.fromRunnable(loadSensitivity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(sensitivityObserver)

    }

    private val scanCallback = object : ScanCallback(){
        @SuppressLint("SimpleDateFormat", "CheckResult")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            val getResult = Runnable {
                val name = result!!.device.name
                val mac = result.device.address
                Log.d("MMV", "$name ($mac)")
                val advertisingData: ByteArray = result.scanRecord!!.bytes
                val stringBuilder = StringBuilder(advertisingData.size)
                for (byteChar in advertisingData.indices) {
                    stringBuilder.append(String.format("%02x",(advertisingData[byteChar].toInt() and 0xFF)))
                }
                if (stringBuilder.contains("0201060303e1ff1216e1ff")){
                    Log.d("MMV", "Head Ok")
                    val battery: Int = ((advertisingData[13]).toInt())
                    var x1: Int = ((advertisingData[14]).toInt())
                    var x2: Int = ((advertisingData[15]).toInt())
                    if (x1 > 250){
                        x1 -= 255
                        x2 = abs(x2-255)
                    }
                    val x = "$x1.$x2"
                    var y1: Int = ((advertisingData[16]).toInt())
                    var y2: Int = ((advertisingData[17]).toInt())
                    if (y1 > 250){
                        y1 -= 255
                        y2 = abs(y2-255)
                    }
                    val y = "$y1.$y2"

                    if ((x2 - sensitivity!! > 0) or (y2 - sensitivity!! > 0)){
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
                        val currentDate = sdf.format(Date())
                        Log.d("MMV", "battery: $battery, X: $x, Y: $y," +
                                " $name ($mac)")

                        val database = DbHelper(this@MainActivity).writableDatabase
                        dbTable.addLog(database, mac, currentDate)
                        database.close()
                    }
                }
            }

            Completable.fromRunnable(getResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(scanResultObserver)

        }
    }

    private val sensitivityObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "sensitivity OK")
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(this@MainActivity, "Ошибка sensitivity", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    private val scanResultObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scanner.stopScan(scanCallback)
    }
}
