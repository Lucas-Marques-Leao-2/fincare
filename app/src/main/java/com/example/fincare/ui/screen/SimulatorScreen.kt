package com.example.fincare.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.ui.components.BarChart
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareBlue
import com.example.fincare.ui.theme.FincareEarning

fun parseAmount(text: String): Double? {
    var clean = text.replace("R$", "").trim()
    if (clean.contains(",")) {
        clean = clean.replace(".", "").replace(",", ".")
    }
    return clean.toDoubleOrNull()
}

@Composable
fun SimulatorScreen(navController: NavController) {
    var sigla by remember { mutableStateOf("CDI") }
    var rate by remember { mutableStateOf(0.0) }
    var loadingRate by remember { mutableStateOf(true) }
    var rateError by remember { mutableStateOf(false) }

    var initial by remember { mutableStateOf("1000") }
    var monthly by remember { mutableStateOf("200") }
    var years by remember { mutableStateOf("5") }

    LaunchedEffect(sigla) {
        loadingRate = true
        rateError = false
        try {
            rate = RetrofitHelper().rateApi.findBySigla(sigla).valor ?: 0.0
        } catch (e: Exception) {
            rateError = true
        }
        loadingRate = false
    }

    val initialValue = parseAmount(initial)
    val monthlyValue = parseAmount(monthly)
    val yearsValue = years.toIntOrNull()
    val validInput = initialValue != null && monthlyValue != null &&
        yearsValue != null && yearsValue in 1..10

    var balance = 0.0
    var invested = 0.0
    val chartLabels = mutableListOf<String>()
    val chartValues = mutableListOf<Double>()

    if (validInput && !loadingRate && !rateError) {
        val monthlyRate = Math.pow(1 + rate / 100, 1.0 / 12) - 1
        balance = initialValue!!
        invested = initialValue

        for (month in 1..yearsValue!! * 12) {
            balance = balance * (1 + monthlyRate) + monthlyValue!!
            invested += monthlyValue
            if (month % 12 == 0) {
                chartLabels.add("${month / 12}")
                chartValues.add(balance)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader("Simulador")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("Rende quanto?", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextDark)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (option in listOf("CDI", "SELIC")) {
                    if (sigla == option) {
                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                            onClick = { sigla = option },
                        ) {
                            Text("100% do $option")
                        }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { sigla = option },
                        ) {
                            Text("100% do $option")
                        }
                    }
                }
            }

            if (loadingRate) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FincareBlue)
                }
            } else if (rateError) {
                Text("Não foi possível buscar a taxa. Verifique a internet.", color = TextMuted)
            } else {
                Text(
                    "$sigla hoje: ${"%.2f".format(rate).replace(".", ",")}% ao ano (BrasilAPI)",
                    color = TextMuted,
                    fontSize = 13.sp,
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = initial,
                singleLine = true,
                onValueChange = { initial = it },
                label = { Text("Valor inicial (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = monthly,
                singleLine = true,
                onValueChange = { monthly = it },
                label = { Text("Aporte por mês (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = years,
                singleLine = true,
                onValueChange = { years = it },
                label = { Text("Prazo (anos)") },
                supportingText = { Text("De 1 a 10 anos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
            )

            if (!validInput) {
                Text("Preencha os valores com números, ex.: 1500,00", color = TextMuted)
            } else if (chartValues.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text("Valor final", color = TextMuted, fontSize = 13.sp)
                        Text(
                            formatBrl(balance),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                        )
                        InfoLine("Total investido", formatBrl(invested), TextDark)
                        InfoLine("Rendimento", formatBrl(balance - invested), FincareEarning)

                        Text("Saldo ao fim de cada ano", color = TextMuted, fontSize = 13.sp)
                        BarChart(labels = chartLabels, values = chartValues)
                    }
                }

                Text(
                    "Simulação com a taxa de hoje fixa e sem impostos.",
                    color = TextMuted,
                    fontSize = 12.sp,
                )
            }
        }
    }
}
