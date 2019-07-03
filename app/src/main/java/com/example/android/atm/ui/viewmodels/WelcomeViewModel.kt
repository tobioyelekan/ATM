package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.db.Customer
import com.example.android.atm.repos.WelcomeRepo

class WelcomeViewModel(private val customerId: Long, application: Application) : ViewModel() {

    enum class TransactionType { TYPE_BALANCE, TYPE_WITHDRAW }

    private val database = BankDatabase.getInstance(application).customerDao
    private val repo = WelcomeRepo(database)

    val _transactionType = MutableLiveData<TransactionType?>()

    private val _getTransactionType = MutableLiveData<TransactionType>()
    val getTransactionType: LiveData<TransactionType>
        get() = _getTransactionType

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?>
        get() = _status

    private val customer = repo.customer

    init {
        getCustomer(customerId)
    }

    private fun getCustomer(customerId: Long) {
        repo.getCustomer(customerId)
    }

    val customerName = Transformations.map(customer) {
        it.name
    }

    fun setTransactionType(transactionType: TransactionType) {
        _transactionType.value = transactionType
    }

    fun getTransactionType() {
        when {
            _transactionType.value != null -> _getTransactionType.value = _transactionType.value
            else -> _status.value = "please choose a choice"
        }
    }

    fun endStatus() {
        _status.value = null
    }

    fun cancel() {
        _status.value = "cancel"
    }

    fun removeTransactionType() {
        _getTransactionType.value = null
    }

}
