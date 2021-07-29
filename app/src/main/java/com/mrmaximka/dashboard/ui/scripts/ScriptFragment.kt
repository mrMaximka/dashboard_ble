package com.mrmaximka.dashboard.ui.scripts

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mrmaximka.dashboard.R
import com.mrmaximka.dashboard.model.ScriptList
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_script.*


class ScriptFragment : Fragment(), UpdateInterface {

    private lateinit var viewModel: ScriptViewModel
    private lateinit var scriptsList: RecyclerView
    private lateinit var adapter: ScriptAdapter
    private lateinit var btnAdd: Button
    private lateinit var pathInterface: PathInterface
    var list: ArrayList<ScriptList> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_script, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ScriptViewModel::class.java)

        Handler().postDelayed({
            initView()
            setViewSettings()
            loadScripts()
        },200)
    }

    @SuppressLint("CheckResult")
    private fun loadScripts() {
        val loadBrands = Runnable {
            Log.d("MMV", "Loading")
            list = viewModel.loadScripts(context)
        }

        Completable.fromRunnable(loadBrands)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(scriptObserver)
    }

    private fun setViewSettings() {
        adapter = ScriptAdapter(context, this)
        scriptsList.adapter = adapter

        btnAdd.setOnClickListener { addScript() }
    }

    private fun initView() {
        scriptsList = view!!.findViewById(R.id.scripts_list)
        btnAdd = view!!.findViewById(R.id.add_script_btn)
    }

    override fun update() {
        pbScript.visibility = View.VISIBLE
        scripts_list.visibility = View.GONE
        loadScripts()
    }

    @SuppressLint("CheckResult")
    private fun addScript() {
        viewModel.addScript(adapter)
    }

    override fun getVideo(pathInterface: PathInterface){
        this.pathInterface = pathInterface
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && data != null){
            val selectedImageUri: Uri = data.data!!
            val selectedImagePath = getPath(selectedImageUri)
            pathInterface.setPath(selectedImagePath)
        }
    }


    @SuppressLint("Recycle")
    private fun getPath(uri: Uri): String{
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor = context!!.contentResolver.query(uri, projection, null, null, null)!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private val scriptObserver: CompletableObserver = object : CompletableObserver {
        override fun onComplete() {
            Log.d("MMV", "Complete")
            adapter.setElements(list)
            pbScript.visibility = View.GONE
            scripts_list.visibility = View.VISIBLE
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            Toast.makeText(context, "Ошибка при загрузке сценариев", Toast.LENGTH_LONG).show()
            Log.d("MMV", String.format("${e.message}"))
        }
    }
}