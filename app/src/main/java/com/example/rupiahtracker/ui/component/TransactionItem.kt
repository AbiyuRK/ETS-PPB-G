package com.example.rupiahtracker.ui.component

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rupiahtracker.data.Transaction
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddTransaction(
    viewModel: TransactionViewModel = viewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // State dan data
    var transactionType by remember { mutableStateOf("Pemasukan") }
    val transactionTypes = listOf("Pemasukan", "Pengeluaran")

    var selectedCategory by remember { mutableStateOf("Makanan") }
    val categories = listOf("Tabungan", "Transfer", "Makanan", "Transportasi", "Pulsa", "Listrik", "Air", "Lainnya")

    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Tanggal transaksi
    val calendar = remember { Calendar.getInstance() }
    var selectedDateMillis by remember { mutableLongStateOf(calendar.timeInMillis) }

    val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }

    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            selectedDateMillis = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // UI Layout
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Form Transaksi") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tipe Transaksi")
            transactionTypes.forEach { type ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = transactionType == type,
                        onClick = { transactionType = type }
                    )
                    Text(type)
                }
            }

            Text("Kategori")
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(selectedCategory)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text("Jumlah Uang")
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Contoh: 100000") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tanggal")
            OutlinedButton(onClick = { datePickerDialog.show() }) {
                Text(text = dateFormat.format(Date(selectedDateMillis)))
            }

            Button(
                onClick = {
                    if (amount.isNotBlank()) {
                        val newTransaction = Transaction(
                            type = transactionType.uppercase(),
                            category = selectedCategory,
                            amount = amount.toDouble(),
                            date = selectedDateMillis
                        )
                        viewModel.insert(newTransaction)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}

@Composable
fun EditTransaction(
    transactionId: Long? = null,
    viewModel: TransactionViewModel = viewModel(),
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current

    // State dan data
    var transactionType by remember { mutableStateOf("Pemasukan") }
    val transactionTypes = listOf("Pemasukan", "Pengeluaran")

    var selectedCategory by remember { mutableStateOf("Makanan") }
    val categories = listOf("Makanan", "Transportasi", "Pulsa", "Listrik", "Air", "Lainnya")

    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Tanggal transaksi
    val calendar = remember { Calendar.getInstance() }
    var selectedDateMillis by remember { mutableLongStateOf(calendar.timeInMillis) }

    val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }

    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            selectedDateMillis = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(transactionId) {
        transactionId?.let { id ->
            val transaction = viewModel.getTransactionById(id)
            transaction?.let {
                transactionType = it.type
                selectedCategory = it.category
                amount = it.amount.toString()
                selectedDateMillis = it.date
            }
        }
    }

    // UI Layout
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Form Transaksi") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tipe Transaksi")
            transactionTypes.forEach { type ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = transactionType == type,
                        onClick = { transactionType = type }
                    )
                    Text(type)
                }
            }

            Text("Kategori")
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(selectedCategory)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text("Jumlah Uang")
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Contoh: 100000") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tanggal")
            OutlinedButton(onClick = { datePickerDialog.show() }) {
                Text(text = dateFormat.format(Date(selectedDateMillis)))
            }

            Button(onClick = {
                val transaction = Transaction(
                    id = transactionId ?: 0,
                    type = transactionType.uppercase(),
                    category = selectedCategory,
                    amount = amount.toDouble(),
                    date = selectedDateMillis
                )

                if (transactionId != null) {
                    viewModel.update(transaction)
                }
                onSaveSuccess()
            }) {
                Text("Simpan")
            }

            Button(onClick = {
                val transaction = Transaction(
                    id = transactionId ?: 0,
                    type = transactionType.uppercase(),
                    category = selectedCategory,
                    amount = amount.toDouble(),
                    date = selectedDateMillis
                )

                if (transactionId != null) {
                    viewModel.delete(transaction)
                }
                onSaveSuccess()
            },
                colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text("Hapus")
            }
        }
    }
}