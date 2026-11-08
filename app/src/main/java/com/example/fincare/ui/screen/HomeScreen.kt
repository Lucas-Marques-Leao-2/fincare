package com.example.fincare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.db.findAllSeedingSampleIfEmpty
import com.example.fincare.domain.Investment
import com.example.fincare.domain.Rate
import com.example.fincare.domain.Transaction
import com.example.fincare.ui.components.BarChart
import com.example.fincare.ui.components.CardTransaction
import com.example.fincare.ui.components.CategoryBar
import com.example.fincare.ui.components.chartColors
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareBlue
import com.example.fincare.ui.theme.FincareExpense

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current

    val transactionsList = remember { mutableStateListOf<Transaction>() }
    val investmentsList = remember { mutableStateListOf<Investment>() }
    val ratesList = remember { mutableStateListOf<Rate>() }
    var loadingRates by remember { mutableStateOf(true) }
    var ratesError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val db = DatabaseHelper.getInstance(context)

        transactionsList.clear()
        transactionsList.addAll(db.transactionDao().findAll())

        investmentsList.clear()
        investmentsList.addAll(db.investmentDao().findAllSeedingSampleIfEmpty())
    }

    LaunchedEffect(Unit) {
        try {
            val rates = RetrofitHelper().rateApi.findAll()
            ratesList.clear()
            ratesList.addAll(rates)
        } catch (e: Exception) {
            ratesError = true
        }
        loadingRates = false
    }

    val expenses = transactionsList.filter { it.type == "Despesa" }
    val totalExpenses = expenses.sumOf { it.amount }
    val totalEarnings = transactionsList.filter { it.type == "Receita" }.sumOf { it.amount }
    val balance = totalEarnings - totalExpenses

    val categories = expenses
        .groupBy { it.category }
        .map { (category, list) -> category to list.sumOf { it.amount } }
        .sortedByDescending { it.second }

    var spentProgress = 0f
    if (totalEarnings > 0) {
        spentProgress = (totalExpenses / totalEarnings).toFloat()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FincareBlue)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Saldo", color = Color.White, fontSize = 13.sp)
                    Text(formatBrl(balance), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.clickable { navController.navigateTab("transactionExploreScreen") },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Detalhes", color = Color.White, fontSize = 13.sp)
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White)
                }
            }

            SectionTitle("Despesas")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("Total gasto", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextDark)
                    Text(formatBrl(totalExpenses), color = FincareExpense, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(
                        progress = { spentProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFFFFCB2F),
                        trackColor = Color(0xFFFDE2E2),
                    )
                    Text("Total de receitas: ${formatBrl(totalEarnings)}", color = TextMuted, fontSize = 12.sp)
                }
            }

            SectionTitle("Por categoria")
            if (categories.isEmpty()) {
                EmptyText("Nenhuma despesa cadastrada")
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CategoryBar(values = categories.map { it.second })

                    for (i in 0 until minOf(4, categories.size)) {
                        CategoryItem(
                            name = categories[i].first,
                            amount = categories[i].second,
                            color = chartColors[i % chartColors.size],
                        )
                    }
                }
            }

            SectionTitle("Últimas transações")
            if (transactionsList.isEmpty()) {
                EmptyText("Nenhuma transação cadastrada")
            }
            for (transaction in transactionsList.reversed().take(4)) {
                CardTransaction(transaction)
            }
            SeeMore { navController.navigateTab("transactionExploreScreen") }

            SectionTitle("Rendimentos")
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    formatBrl(investmentsList.sumOf { it.currentValue }),
                    color = FincareBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                val top = investmentsList.sortedByDescending { it.currentValue }.take(5)
                BarChart(
                    labels = top.map { it.name.take(6) },
                    values = top.map { it.currentValue },
                )
            }
            for (investment in investmentsList.sortedByDescending { it.currentValue }.take(3)) {
                SimpleRow(investment.name, investment.type, formatBrl(investment.currentValue))
            }
            SeeMore { navController.navigateTab("investmentsScreen") }

            SectionTitle("Taxas de referência")
            if (loadingRates) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = FincareBlue)
                }
            } else if (ratesError) {
                EmptyText("Não foi possível carregar as taxas")
            } else {
                for (rate in ratesList) {
                    SimpleRow(rate.nome ?: "-", "Taxa anual (BrasilAPI)", "${rate.valor ?: 0.0}%")
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }

        ExtendedFloatingActionButton(
            onClick = { navController.navigate("transactionFormScreen") },
            containerColor = FincareBlue,
            contentColor = Color.White,
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            text = { Text("Nova") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextDark,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp),
    )
}

@Composable
fun EmptyText(text: String) {
    Text(text, color = TextMuted, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
}

@Composable
fun SeeMore(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            "Ver mais",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = TextDark,
            modifier = Modifier.clickable { onClick() },
        )
    }
}

@Composable
fun CategoryItem(name: String, amount: Double, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            Text(name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(formatBrl(amount), fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextDark)
            Text(name, color = TextMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun SimpleRow(title: String, subtitle: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextDark)
            Text(subtitle, color = TextMuted, fontSize = 12.sp)
        }
        Text(value, color = FincareBlue, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}
