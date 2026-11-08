package com.example.fincare.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.fincare.db.findAllSeedingSampleIfEmpty
import com.example.fincare.domain.Investment
import com.example.fincare.ui.components.BarChart
import com.example.fincare.ui.components.PercentRing
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareBlue
import com.example.fincare.ui.theme.FincareEarning
import com.example.fincare.ui.theme.FincareExpense

val investmentTypes = listOf(
    "Renda fixa",
    "Renda variável",
    "Tesouro direto",
    "Poupança",
    "Fundos de investimento",
    "Cripto",
    "Debêntures",
)

val monthNames = listOf("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez")

val TextDark = Color(0xFF3A3F4B)
val TextMuted = Color(0xFF9A9FAD)

@Composable
fun InvestmentsScreen(navController: NavController) {
    val context = LocalContext.current

    val investmentsList = remember { mutableStateListOf<Investment>() }
    var search by remember { mutableStateOf("") }
    var biggestFirst by remember { mutableStateOf(true) }
    var expandedType by remember { mutableStateOf("Renda fixa") }

    LaunchedEffect(Unit) {
        val investments = DatabaseHelper.getInstance(context)
            .investmentDao()
            .findAllSeedingSampleIfEmpty()

        investmentsList.clear()
        investmentsList.addAll(investments)
        Toast.makeText(context, "Investimentos carregados", Toast.LENGTH_SHORT).show()
    }

    val filtered = investmentsList.filter { investment ->
        investment.name.contains(search, ignoreCase = true) ||
            investment.type.contains(search, ignoreCase = true)
    }
    val total = filtered.sumOf { it.currentValue }

    var types = investmentTypes.sortedBy { type ->
        filtered.filter { it.type == type }.sumOf { it.currentValue }
    }
    if (biggestFirst) {
        types = types.reversed()
    }

    val byMonth = filtered
        .sortedWith(compareBy({ it.year }, { it.month }))
        .groupBy { it.month }
    val chartLabels = byMonth.keys.map { month -> monthNames[month - 1] }
    val chartValues = byMonth.values.map { list -> list.sumOf { it.amountInvested } }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader("Investimentos")

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = search,
                        onValueChange = { search = it },
                        singleLine = true,
                        placeholder = { Text("Buscar investimento") },
                        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                    )
                    IconButton(onClick = { biggestFirst = !biggestFirst }) {
                        Icon(
                            if (biggestFirst) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Ordenar",
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = formatBrl(total),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                        )
                        Text("Valor investido por mês", color = TextMuted, fontSize = 13.sp)
                        BarChart(labels = chartLabels, values = chartValues)
                    }
                }
            }

            items(types) { type ->
                InvestmentGroup(
                    type = type,
                    investments = filtered.filter { it.type == type },
                    total = total,
                    expanded = expandedType == type,
                    onClick = {
                        expandedType = if (expandedType == type) "" else type
                    },
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun InvestmentGroup(
    type: String,
    investments: List<Investment>,
    total: Double,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val groupValue = investments.sumOf { it.currentValue }
    val groupInvested = investments.sumOf { it.amountInvested }
    val gain = groupValue - groupInvested

    var percent = 0.0
    if (total > 0) {
        percent = groupValue / total * 100
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PercentRing(percent)
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Text(type, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextDark)
                Text(formatBrl(groupValue), color = TextMuted, fontSize = 13.sp)
            }
            Icon(
                if (expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = FincareBlue,
            )
        }

        if (expanded && investments.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                InfoLine("Ganho/Perda", formatBrl(gain), if (gain >= 0) FincareEarning else FincareExpense)
                InfoLine("Valor investido", formatBrl(groupInvested), TextMuted)

                for (investment in investments) {
                    InvestmentCard(investment)
                }
            }
        }
    }
}

@Composable
fun InvestmentCard(investment: Investment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE6E8EC), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(investment.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextDark)
        InfoLine("Quantidade", investment.quantity.toString(), TextDark)
        InfoLine("Preço médio", formatBrl(investment.averagePrice), TextDark)
        InfoLine("Valor atual", formatBrl(investment.currentValue), TextDark)
        InfoLine(
            "Ganho/Perda",
            formatBrl(investment.gainLoss),
            if (investment.gainLoss >= 0) FincareEarning else FincareExpense,
        )
    }
}

@Composable
fun InfoLine(label: String, value: String, valueColor: Color) {
    Row {
        Text(label, color = TextMuted, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = valueColor, fontSize = 13.sp)
    }
}

@Composable
fun ScreenHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(FincareBlue)
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(title, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    }
}
