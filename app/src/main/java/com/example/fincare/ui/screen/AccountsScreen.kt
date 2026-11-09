package com.example.fincare.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.db.findAllSeedingSampleIfEmpty
import com.example.fincare.domain.Account
import com.example.fincare.ui.components.DonutChart
import com.example.fincare.ui.components.chartColors
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareBlue
import com.example.fincare.ui.theme.FincareEarning

@Composable
fun AccountsScreen(navController: NavController) {
    val context = LocalContext.current

    val accountsList = remember { mutableStateListOf<Account>() }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val accounts = DatabaseHelper.getInstance(context)
            .accountDao()
            .findAllSeedingSampleIfEmpty()

        accountsList.clear()
        accountsList.addAll(accounts)
        loading = false
    }

    val total = accountsList.sumOf { it.balance }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ScreenHeader("Minhas contas")

            SectionTitle("Saldo")

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = FincareBlue)
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    DonutChart(
                        values = accountsList.map { it.balance },
                        centerText = formatBrl(total),
                    )
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    for (pair in accountsList.withIndex().chunked(2)) {
                        Row {
                            for ((i, account) in pair) {
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryItem(
                                        name = account.name,
                                        amount = account.balance,
                                        color = chartColors[i % chartColors.size],
                                    )
                                }
                            }
                            if (pair.size == 1) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }

                for ((i, account) in accountsList.withIndex()) {
                    AccountCard(account, chartColors[i % chartColors.size])
                }
            }

            Spacer(modifier = Modifier.height(88.dp))
        }

        ExtendedFloatingActionButton(
            onClick = { navController.navigate("accountFormScreen") },
            containerColor = FincareBlue,
            contentColor = Color.White,
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            text = { Text("Nova conta") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )
    }
}

@Composable
fun AccountCard(account: Account, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(account.name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                }
                Text(
                    account.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = TextDark,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                )
                Text(account.kind, color = TextMuted, fontSize = 13.sp)
            }

            HorizontalDivider(color = Color(0xFFE6E8EC))

            Row(verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Saldo atual", color = TextMuted, fontSize = 11.sp)
                    Text(formatBrl(account.balance), fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextDark)
                    Text(account.bankName, color = TextMuted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (account.main) {
                    Text("Conta principal", color = FincareEarning, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
