package com.example.rupiahtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rupiahtracker.data.AppDatabase
import com.example.rupiahtracker.repository.TransactionRepository
import com.example.rupiahtracker.ui.screen.TransactionScreen
import com.example.rupiahtracker.ui.theme.RupiahTrackerTheme
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModel
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).transactionDao()
        val repository = TransactionRepository(dao)
        val viewModelFactory = TransactionViewModelFactory(repository)

        setContent {
            RupiahTrackerTheme {
                val viewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
                AppContent(viewModel)
            }
        }
    }
}

@Composable
fun AppContent(viewModel: TransactionViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        TransactionScreen(viewModel = viewModel)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RupiahTrackerPreview() {
//    RupiahTrackerTheme {
//        AppContent()
//    }
//}