package com.example.fincare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.User
import com.example.fincare.ui.components.formatBrl
import com.example.fincare.ui.theme.FincareBlue
import com.example.fincare.ui.theme.FincareExpense

var loggedEmail = ""

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current

    var user by remember { mutableStateOf<User?>(null) }
    var transactionsCount by remember { mutableStateOf(0) }
    var accountsCount by remember { mutableStateOf(0) }
    var accountsTotal by remember { mutableStateOf(0.0) }
    var investmentsTotal by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        val db = DatabaseHelper.getInstance(context)
        user = db.userDao().findByEmail(loggedEmail)
        transactionsCount = db.transactionDao().findAll().size
        val accounts = db.accountDao().findAll()
        accountsCount = accounts.size
        accountsTotal = accounts.sumOf { it.balance }
        investmentsTotal = db.investmentDao().findAll().sumOf { it.currentValue }
    }

    val name = user?.name ?: "Usuário"

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader("Perfil")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(FincareBlue, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(name.take(1).uppercase(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

            Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text(user?.email ?: "", color = TextMuted)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InfoLine("Telefone", user?.phone ?: "-", TextDark)
                    InfoLine("Estado", user?.state ?: "-", TextDark)
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
                    Text("Seu resumo", fontWeight = FontWeight.SemiBold, color = TextDark)
                    InfoLine("Patrimônio total", formatBrl(accountsTotal + investmentsTotal), FincareBlue)
                    InfoLine("Em contas", formatBrl(accountsTotal), TextDark)
                    InfoLine("Em investimentos", formatBrl(investmentsTotal), TextDark)
                    InfoLine("Contas cadastradas", "$accountsCount", TextDark)
                    InfoLine("Transações registradas", "$transactionsCount", TextDark)
                }
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    loggedEmail = ""
                    navController.navigate("signInScreen") {
                        popUpTo(0)
                    }
                },
            ) {
                Text("Sair", color = FincareExpense)
            }
        }
    }
}
