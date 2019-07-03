package com.example.android.atm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.atm.util.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Customer::class, Transaction::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BankDatabase : RoomDatabase() {

    abstract val customerDao: CustomerDao
    abstract val transactionDaoo: TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: BankDatabase? = null
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        fun getInstance(context: Context): BankDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BankDatabase::class.java,
                        "bank_database"
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            coroutineScope.launch {
                                insertDatabase(context)
                            }
                        }
                    })
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}

fun insertDatabase(context: Context) {
    BankDatabase.getInstance(context).customerDao.insertAll(Customer.populateData())
}