package com.example.rupiahtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.rupiahtracker.data.AppDatabase
import com.example.rupiahtracker.repository.TransactionRepository
import com.example.rupiahtracker.ui.screen.TransactionScreen
import com.example.rupiahtracker.ui.theme.RupiahTrackerTheme
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModel
import com.example.rupiahtracker.ui.viewmodel.TransactionViewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rupiahtracker.ui.component.AddTransaction
import com.example.rupiahtracker.ui.component.EditTransaction

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
fun AppNavigation(viewModel: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        // Halaman utama
        composable("home") {
            TransactionScreen(
                viewModel = viewModel,
                onAddTransactionClick = { navController.navigate("form") },
                onEditTransactionClick = { transactionId ->
                    navController.navigate("form/$transactionId")
                }
            )
        }

        // Form tambah transaksi
        composable("form") {
            AddTransaction(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Form edit transaksi
        composable(
            "form/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId")
            EditTransaction(
                viewModel = viewModel,
                transactionId = transactionId,
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun AppContent(viewModel: TransactionViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AppNavigation(viewModel)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RupiahTrackerPreview() {
//    RupiahTrackerTheme {
//        AppContent()
//    }
//}