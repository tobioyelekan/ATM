package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.db.Customer
import com.example.android.atm.repos.WelcomeRepo
import com.example.android.atm.repos.WithdrawRepo
import kotlinx.coroutines.*

class WithdrawViewModel(private val customerId: Long, application: Application) : ViewModel() {

    private val cutomerDao = BankDatabase.getInstance(application).customerDao
    private val transactionDao = BankDatabase.getInstance(application).transactionDaoo

    private val repo = WithdrawRepo(cutomerDao, transactionDao)

    private val job = Job()
    private val viewModelScope = CoroutineScope(job + Dispatchers.Main)

    private val _amount = MutableLiveData<String>()
    private val _customer = MutableLiveData<Customer>()

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?>
        get() = _status

    private val _trans_id = MutableLiveData<Long?>()
    val trans_id: LiveData<Long?>
        get() = _trans_id

    fun setAmount(amount: String) {
        _amount.value = amount
    }

    fun getAmount() {
        when {
            _amount.value != null -> processWithDraw(_amount.value!!)
            else -> _status.value = "please choose an amount"
        }
    }

    private fun processWithDraw(amount: String) {
        when (amount) {
            "other" -> {
                _status.value = "open_other_amount_dialog"
            }
            else -> {
                canWithDraw(amount.toDouble())
            }
        }
    }

    private fun canWithDraw(amount: Double) {
        viewModelScope.launch {
            val customer = repo.withdraw(customerId, amount)
            if (customer != null) {
                _customer.value = customer
                _status.value = "open_progress_dialog"
            } else {
                _status.value = "Insufficient fund"
            }
        }
    }

    fun debitCustomer() {
        viewModelScope.launch {
            val customer = _customer.value
            val amount = _amount.value
            repo.debitCustomer(customer!!, amount!!.toDouble())
            delay(1000)
            val transId = repo.saveDebit(customer, amount.toDouble())
            _trans_id.value = transId
        }
    }

    fun withdrawAmount(amount: String) {
        _amount.value = amount
        canWithDraw(amount.toDouble())
    }

    fun endStatus() {
        _status.value = null
    }

    fun cancel(){
        _status.value = "cancel"
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun cleanTransId() {
        _trans_id.value = null
    }


}
