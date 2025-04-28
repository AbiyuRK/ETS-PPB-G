package com.example.rupiahtracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.rupiahtracker.data.AppDatabase
import com.example.rupiahtracker.data.Transaction
import com.example.rupiahtracker.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransaction: LiveData<List<Transaction>>
    val totalIncome: LiveData<Double>
    val totalOutcome: LiveData<Double>

    init {
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        allTransaction = repository.allTransactions
        totalIncome = repository.totalIncome
        totalOutcome = repository.totalOutcome
    }

    fun insert(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(transaction)
    }
}