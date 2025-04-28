package com.example.rupiahtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'PEMASUKAN'")
    fun getTotalIncome(): LiveData<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'PENGELUARAN'")
    fun getTotalOutcome(): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    fun getTransactionsByType(type: String): LiveData<List<Transaction>>
}
