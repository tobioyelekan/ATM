package com.example.android.atm.repos

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.atm.db.CustomerDao

class WelcomeRepo(private val customerDao: CustomerDao) {

    private val _customerId = MutableLiveData<Long>()

    val customer = Transformations.switchMap(_customerId) {
        customerDao.getCustomer(it)
    }

    fun getCustomer(customerId: Long) {
        _customerId.value = customerId
    }
}