package com.example.fincare.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.Transaction
import com.example.fincare.ui.components.CardTransaction
import com.example.fincare.ui.theme.FincareBlue
import java.util.Locale

@Composable
fun TransactionExploreScreen(navController: NavController) {

    val transactionsList = remember { mutableStateListOf<Transaction>() }
    val search = remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val transactions = DatabaseHelper
            .getInstance(context)
            .transactionDao()
            .findAll()

        transactionsList.clear()
        transactionsList.addAll(transactions)
        Toast.makeText(context, "Transações carregadas", Toast.LENGTH_SHORT).show()
    }

    val filtered = transactionsList.filter { transaction ->
        transaction.description.contains(search.value, ignoreCase = true) ||
            transaction.category.contains(search.value, ignoreCase = true)
    }

    val expenses = transactionsList
        .filter { it.type == "Despesa" }
        .sumOf { it.amount }
    val earnings = transactionsList
        .filter { it.type == "Receita" }
        .sumOf { it.amount }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FincareBlue)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Extrato",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Despesas ${formatTotal(expenses)}   ·   Receitas ${formatTotal(earnings)}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                value = search.value,
                onValueChange = { search.value = it },
                singleLine = true,
                label = { Text("Buscar transação") },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = "Buscar")
                },
                shape = RoundedCornerShape(12.dp),
            )

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { navController.navigate("reportScreen") },
            ) {
                Text("Ver relatório do mês")
            }

            Text(
                text = "Últimas transações",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )

            if (filtered.isEmpty()) {
                Text(
                    text = "Nenhuma transação cadastrada",
                    color = Color(0xFF6C6C6C),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                )
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filtered) { transaction ->
                    CardTransaction(transaction)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                    onClick = {
                        navController.navigate("transactionFormScreen")
                    },
                ) {
                    Text("Nova transação")
                }
            }
        }
    }
}

private fun formatTotal(amount: Double): String {
    val formatted = String.format(Locale("pt", "BR"), "%.2f", amount)
    return "R$ $formatted"
}
