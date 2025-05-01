package com.example.rupiahtracker.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rupiahtracker.data.Transaction
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModel
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel = viewModel(),
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (Long) -> Unit
) {
    val transactions by viewModel.allTransaction.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()
    var selectedTransactionId by remember { mutableStateOf<Long?>(null) }
    val balance = totalIncome - totalOutcome

    val localeID =  Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            // Saldo
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Saldo Anda", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = numberFormat.format(balance).toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(Color.Green)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Pemasukan", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = numberFormat.format(totalIncome).toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(Color.Red)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Pengeluaran", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = numberFormat.format(totalOutcome).toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransactionPieChart(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Daftar Transaksi
            Text(text = "Transaksi Terbaru", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        isSelected = selectedTransactionId == transaction.id,
                        onClick = { selectedTransactionId = transaction.id },
                        onEditClick = { onEditTransactionClick(transaction.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(transaction.date))
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = transaction.category, style = MaterialTheme.typography.bodyLarge)
                    Text(text = formattedDate, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = if (transaction.type == "PEMASUKAN") "+" + numberFormat.format(transaction.amount)
                    else "-" + numberFormat.format(transaction.amount),
                    color = if (transaction.type == "PEMASUKAN") Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tombol edit hanya muncul jika item dipilih
            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Edit")
                }
            }
        }
    }
}

@Composable
fun TransactionPieChart(viewModel: TransactionViewModel = viewModel()) {
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalOutcome by viewModel.totalOutcome.collectAsState()

    AndroidView(factory = { context ->
        val pieChart = PieChart(context)

        val entries = listOf(
            PieEntry(totalIncome.toFloat(), "Pemasukan"),
            PieEntry(totalOutcome.toFloat(), "Pengeluaran")
        )

        val dataSet = PieDataSet(entries, "Transaksi")
        dataSet.colors = listOf(
            android.graphics.Color.rgb(76, 175, 80),
            android.graphics.Color.rgb(244, 67, 54)
        )
        dataSet.valueTextSize = 14f

        val pieData = PieData(dataSet)
        pieChart.data = pieData

        pieChart.description = Description().apply { text = "" }
        pieChart.centerText = "Transaksi"
        pieChart.animateY(1000)
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelColor(android.graphics.Color.BLACK)
        pieChart.invalidate()

        pieChart
    },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
