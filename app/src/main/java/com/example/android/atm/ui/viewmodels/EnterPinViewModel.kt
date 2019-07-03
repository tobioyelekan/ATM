package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.db.Customer
import com.example.android.atm.repos.EnterPinRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EnterPinViewModel(private val accountNo: String, application: Application) : ViewModel() {

    private val job = Job()
    private val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    private val dao = BankDatabase.getInstance(application).customerDao
    private val repo = EnterPinRepo(dao)

    val pin = MutableLiveData<String>()

    private val _passwordTries = MutableLiveData<Int>()

    init {
        _passwordTries.value = 0
        pin.value = ""
    }

    private val _passwordStatus = MutableLiveData<String>()
    val passwordStatus: LiveData<String>
        get() = _passwordStatus

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?>
        get() = _customer

    fun validatePin() {
        when {
            pin.value!!.isEmpty() -> _passwordStatus.value = "pin cannot be empty"
            pin.value!!.length < 4 -> _passwordStatus.value = "pin is less than 4"
            else -> getCustomer(pin.value!!)
        }
    }

    val passwordTriesMax: LiveData<Boolean> = Transformations.map(_passwordTries) {
        if (it == 3) {
            blockAccount(accountNo)
            true
        } else {
            false
        }
    }

    private fun getCustomer(pin: String) {
        viewModelScope.launch {
            val customer = repo.verifyPin(accountNo, pin.toInt())
            if (customer == null) {
                _passwordStatus.value = "password is wrong"
                _passwordTries.value = _passwordTries.value?.plus(1)
            } else {
                _customer.value = customer
            }
        }
    }

    private fun blockAccount(accountNo: String) {
        viewModelScope.launch {
            repo.blockAccount(accountNo)
        }
    }

    fun endDisplay() {
        _passwordStatus.value = null
    }
}