package com.mrmaximka.dashboard.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "4 Fragment"
    }
    val text: LiveData<String> = _text
}