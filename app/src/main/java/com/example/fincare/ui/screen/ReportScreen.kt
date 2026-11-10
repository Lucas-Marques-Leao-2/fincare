package com.example.fincare.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.Transaction
import com.example.fincare.ui.components.DonutChart
import com.example.fincare.ui.components.chartColors
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareEarning
import com.example.fincare.ui.theme.FincareExpense

fun monthOf(transaction: Transaction): Int {
    return transaction.date.split("/").getOrNull(1)?.trim()?.toIntOrNull() ?: 0
}

@Composable
fun ReportScreen(navController: NavController) {
    val context = LocalContext.current

    val transactionsList = remember { mutableStateListOf<Transaction>() }
    var loading by remember { mutableStateOf(true) }
    var monthIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val transactions = DatabaseHelper.getInstance(context)
            .transactionDao()
            .findAll()
        transactionsList.clear()
        transactionsList.addAll(transactions)
        loading = false
    }

    val months = transactionsList
        .map { monthOf(it) }
        .filter { it in 1..12 }
        .distinct()
        .sorted()

    if (monthIndex >= months.size) {
        monthIndex = months.size - 1
    }
    if (monthIndex < 0) {
        monthIndex = 0
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader("Relatório")

        if (loading) {
            return@Column
        }

        if (months.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cadastre transações com data dd/mm para ver o relatório", color = TextMuted)
            }
            return@Column
        }

        val month = months[monthIndex]
        val monthTransactions = transactionsList.filter { monthOf(it) == month }
        val expenses = monthTransactions.filter { it.type == "Despesa" }
        val totalExpenses = expenses.sumOf { it.amount }
        val totalEarnings = monthTransactions.filter { it.type == "Receita" }.sumOf { it.amount }
        val result = totalEarnings - totalExpenses

        val byCategory = expenses
            .groupBy { it.category }
            .map { entry -> entry.key to entry.value.sumOf { it.amount } }
            .sortedByDescending { it.second }

        val biggest = expenses.maxByOrNull { it.amount }

        var previousExpenses = 0.0
        if (monthIndex > 0) {
            previousExpenses = transactionsList
                .filter { monthOf(it) == months[monthIndex - 1] && it.type == "Despesa" }
                .sumOf { it.amount }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { monthIndex-- }, enabled = monthIndex > 0) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Mês anterior")
                }
                Text(
                    monthNames[month - 1],
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                IconButton(onClick = { monthIndex++ }, enabled = monthIndex < months.size - 1) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Próximo mês")
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InfoLine("Receitas", formatBrl(totalEarnings), FincareEarning)
                    InfoLine("Despesas", formatBrl(totalExpenses), FincareExpense)
                    InfoLine(
                        "Resultado do mês",
                        formatBrl(result),
                        if (result >= 0) FincareEarning else FincareExpense,
                    )
                    if (monthIndex > 0) {
                        val diff = totalExpenses - previousExpenses
                        var text = "Gastou ${formatBrl(diff)} a mais que o mês anterior"
                        if (diff < 0) {
                            text = "Gastou ${formatBrl(-diff)} a menos que o mês anterior"
                        }
                        Text(text, color = TextMuted, fontSize = 13.sp)
                    }
                }
            }

            SectionTitle("Gastos por categoria")

            if (byCategory.isEmpty()) {
                EmptyText("Nenhuma despesa neste mês")
            } else {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    DonutChart(
                        values = byCategory.map { it.second },
                        centerText = formatBrl(totalExpenses),
                    )
                }

                for (i in byCategory.indices) {
                    val percent = (byCategory[i].second / totalExpenses * 100).toInt()
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f)) {
                            CategoryItem(
                                byCategory[i].first,
                                byCategory[i].second,
                                chartColors[i % chartColors.size],
                            )
                        }
                        Text("$percent%", color = TextMuted, fontSize = 13.sp)
                    }
                }
            }

            if (biggest != null) {
                SectionTitle("Maior despesa")
                SimpleRow(biggest.description, "${biggest.category} · ${biggest.date}", formatBrl(biggest.amount))
            }
        }
    }
}
