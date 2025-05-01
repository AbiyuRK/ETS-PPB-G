package com.example.rupiahtracker.repository

import kotlinx.coroutines.flow.Flow
import com.example.rupiahtracker.data.Transaction
import com.example.rupiahtracker.data.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    val totalIncome: Flow<Double?> = transactionDao.getTotalIncome()
    val totalOutcome: Flow<Double?> = transactionDao.getTotalOutcome()

    fun getTransactionsByType(type: String): Flow<List<Transaction>> {
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

    suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)
    }
}