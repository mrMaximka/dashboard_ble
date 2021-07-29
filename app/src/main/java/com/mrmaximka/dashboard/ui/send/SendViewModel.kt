package com.mrmaximka.dashboard.ui.send

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SendViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "5 Fragment"
    }
    val text: LiveData<String> = _text
}