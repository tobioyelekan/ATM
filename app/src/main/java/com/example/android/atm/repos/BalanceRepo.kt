package com.example.android.atm.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.atm.db.CustomerDao
import com.example.android.atm.db.Transaction
import com.example.android.atm.db.TransactionDao

class BalanceRepo(private val customerDao: CustomerDao, private val transactionDao: TransactionDao) {

    private val customerId = MutableLiveData<Long>()

    val balance = Transformations.switchMap(customerId) {
        customerDao.getBalance(it)
    }

    val listOfTransaction: LiveData<List<Transaction>?> = Transformations.switchMap(customerId) {
        transactionDao.getAllTransactions(it)
    }

    fun getBalance(customerId: Long) {
        this.customerId.value = customerId
    }
}