package com.namjackson.firebaseapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namjackson.firebaseapp.util.Event

abstract class BaseViewModel : ViewModel() {

    protected val _showToastEvent = MutableLiveData<Event<String>>()
    val showToastEvent: LiveData<Event<String>>
        get() = _showToastEvent

    protected var _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    protected fun showError(error: String) {
        _isLoading.value = false
        _showToastEvent.value = Event(error)
    }

}