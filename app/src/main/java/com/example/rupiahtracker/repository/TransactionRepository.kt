package com.example.rupiahtracker.repository

import androidx.lifecycle.LiveData
import com.example.rupiahtracker.data.Transaction
import com.example.rupiahtracker.data.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()
    val totalIncome: LiveData<Double> = transactionDao.getTotalIncome()
    val totalOutcome: LiveData<Double> = transactionDao.getTotalOutcome()

    fun getTransactionsByType(type: String): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }

    suspend fun insert(transaction: Transaction) {
        return transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        return transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        return transactionDao.delete(transaction)
    }
}