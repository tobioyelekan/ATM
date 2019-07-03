package com.example.android.atm.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel;
import com.example.android.atm.db.BankDatabase
import com.example.android.atm.repos.BalanceRepo

class BalanceViewModel(private val customerId: Long, application: Application) : ViewModel() {

    private val customerDao = BankDatabase.getInstance(application).customerDao
    private val transactionDaoo = BankDatabase.getInstance(application).transactionDaoo

    private val repo = BalanceRepo(customerDao, transactionDaoo)

    private val _status = MutableLiveData<String?>()
    val status: LiveData<String?>
        get() = _status

    val balance = Transformations.map(repo.balance) {
        it.toString()
    }

    val transactions = repo.listOfTransaction

    val empty = Transformations.map(transactions) {
        it.isNullOrEmpty()
    }

    init {
        getDetails()
    }

    private fun getDetails() {
        repo.getBalance(customerId)
    }

    fun cancel(){
        _status.value = "cancel"
    }
}
