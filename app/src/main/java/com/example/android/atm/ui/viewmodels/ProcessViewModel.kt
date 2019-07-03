package com.example.android.atm.ui.viewmodels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProcessViewModel : ViewModel() {

    companion object {
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 10000L
    }

    private lateinit var timer: CountDownTimer

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    private val _dialogStatus = MutableLiveData<String?>()
    val dialogStatus: LiveData<String?>
        get() = _dialogStatus

    init {
        _progress.value = 0
        startLoading()
    }

    private fun startLoading() {
        timer = object : CountDownTimer(
            COUNTDOWN_TIME,
            ONE_SECOND
        ) {

            override fun onTick(millisUntilFinished: Long) {
                _progress.value = (_progress.value ?: 0) + 1
            }

            override fun onFinish() {
                _progress.value = 100
                _dialogStatus.value = "done"
            }

        }.start()
    }

    fun dismissDialog() {
        _dialogStatus.value = "exit"
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}