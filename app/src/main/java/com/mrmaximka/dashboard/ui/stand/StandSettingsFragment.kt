package com.mrmaximka.dashboard.ui.stand

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.StandSettings
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sensor.*

class StandSettingsFragment : Fragment() {

    private lateinit var viewModel: StandSettingsViewModel
    private lateinit var btnStandSave: Button
    private lateinit var etStandName: EditText
    private lateinit var etStandTimezone: EditText
    private lateinit var etStandDescription: EditText
    private lateinit var etStandVideoDelay: EditText
    private lateinit var etStandWaitTime: EditText
    private lateinit var cbStop: Switch
    private lateinit var cbReplay: Switch
    private lateinit var etStandPlayOne: EditText
    private lateinit var etStandPlayTwo: EditText
    private lateinit var cbBreakTwo: Switch
    private lateinit var etStandSensitivity: EditText
    private lateinit var btnMoveSave: Button

    var model = StandSettings()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_stand, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(StandSettingsViewModel::class.java)
        viewModel.saveStandClick.observe(this, Observer {
            if (it){
                viewModel.saveStandClick.value = false
                val saveStandRun = Runnable {
                    Log.d("MMV", "Loading")
                    viewModel.saveStand(context, etStandName.text, etStandTimezone.text, etStandDescription.text)
                }

                Completable.fromRunnable(saveStandRun)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(saveObserver)

            }
        })

        viewModel.saveMoveClick.observe(this, Observer {
            if (it){
                viewModel.saveMoveClick.value = false
                val saveMoveRun = Runnable {
                    Log.d("MMV", "Loading")
                    viewModel.saveMove(
                        context,
                        etStandVideoDelay.text.toString(),
                        etStandWaitTime.text.toString(),
                        cbStop.isChecked,
                        cbReplay.isChecked,
                        etStandPlayOne.text.toString(),
                        etStandPlayTwo.text.toString(),
                        cbBreakTwo.isChecked,
                        etStandSensitivity.text.toString() )
                }

                Completable.fromRunnable(saveMoveRun)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(saveObserver)
            }
        })

        Handler().postDelayed({
            initView()
            loadStand()
        }, 200)

    }

    @SuppressLint("CheckResult")
    private fun loadStand() {
        val loadStandRun = Runnable {
            Log.d("MMV", "Loading")
            model = viewModel.loadStand(context)
        }

        Completable.fromRunnable(loadStandRun)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(loadStandObserver)
    }

    private fun setStandParams(){
        etStandName.setText(model.name)
        etStandTimezone.setText(model.timezone)
        etStandDescription.setText(model.description)
        etStandVideoDelay .setText(model.delay.toString())
        etStandWaitTime .setText(model.wait_time.toString())
        cbStop.isChecked = model.stop == 1
        cbReplay.isChecked = model.replay == 1
        etStandPlayOne.setText(model.stopPlayOne.toString())
        etStandPlayTwo.setText(model.stopPlayTwo.toString())
        cbBreakTwo.isChecked = model.breakTwo == 1
        etStandSensitivity.setText(model.sensitivity.toString())
    }

    private fun initView() {
        btnStandSave = view!!.findViewById(R.id.save_stand_btn)
        etStandName = view!!.findViewById(R.id.et_stand_name)
        etStandTimezone = view!!.findViewById(R.id.et_stand_timezone)
        etStandDescription = view!!.findViewById(R.id.et_stand_description)
        etStandVideoDelay = view!!.findViewById(R.id.et_stand_video_delay)
        etStandWaitTime = view!!.findViewById(R.id.et_stand_wait_time)
        cbStop = view!!.findViewById(R.id.cb_stand_stop)
        cbReplay = view!!.findViewById(R.id.cb_stand_replay)
        etStandPlayOne = view!!.findViewById(R.id.et_stand_stop_play_one)
        etStandPlayTwo = view!!.findViewById(R.id.et_stand_stop_play_two)
        cbBreakTwo = view!!.findViewById(R.id.cb_stand_break_two)
        etStandSensitivity = view!!.findViewById(R.id.et_stand_sensitivity)
        btnMoveSave = view!!.findViewById(R.id.save_move_btn)

        btnStandSave.setOnClickListener {
            viewModel.onSaveStandClick()
            Toast.makeText(context, "Настройки стенда обновлены", Toast.LENGTH_LONG).show()
        }
        btnMoveSave.setOnClickListener {
            viewModel.onSaveMoveClick()
            Toast.makeText(context, "Настройки воспроизведения обновлены", Toast.LENGTH_LONG).show()
        }
    }

    private val loadStandObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            setStandParams()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке стенда", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }

    private val saveObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при сохранении стенда", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}