package com.example.android.atm.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.atm.db.Customer
import com.example.android.atm.db.CustomerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnterPinRepo(private val customerDao: CustomerDao) {

    suspend fun verifyPin(accountNo: String, pin: Int): Customer? {
        return withContext(Dispatchers.IO) {
            customerDao.verifyPin(accountNo, pin)
        }
    }

    suspend fun blockAccount(accountNo: String) {
        withContext(Dispatchers.IO) {
            customerDao.blockAccount(accountNo)
        }
    }

}