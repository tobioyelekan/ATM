package com.example.android.atm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class OtherAmountViewModel : ViewModel() {

    val amount = MutableLiveData<String>()

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?> = _status

    init {
        amount.value = ""
    }

    fun submitAmount() {
        when {
            amount.value!!.isEmpty() -> _status.value = "cannot be empty"
            amount.value == "0" -> _status.value = "please enter multiple of 500 or 1000"
            !checkAmount(amount.value!!.toInt()) -> _status.value = "please enter multiple of 500 or 1000"
            else -> _status.value = "done"
        }
    }

    private fun checkAmount(amount: Int) = ((amount % 500) == 0) || ((amount % 1000) == 0)

    fun endStatus() {
        _status.value = null
    }

}