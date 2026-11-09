package com.example.fincare.ui.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.Account
import com.example.fincare.domain.Bank
import com.example.fincare.ui.theme.FincareBlue
import kotlinx.coroutines.launch

@Composable
fun AccountFormScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val banksList = remember { mutableStateListOf<Bank>() }
    var loadingBanks by remember { mutableStateOf(true) }
    var banksError by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var kind by remember { mutableStateOf("Manual") }
    var main by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    var selectedBank by remember { mutableStateOf<Bank?>(null) }

    LaunchedEffect(Unit) {
        try {
            val banks = RetrofitHelper().bankApi.findAll()
            banksList.clear()
            banksList.addAll(banks.filter { it.code != null && it.name != null })
        } catch (e: Exception) {
            banksError = true
        }
        loadingBanks = false
    }

    val results = banksList
        .filter { bank ->
            (bank.name ?: "").contains(search, ignoreCase = true) ||
                bank.code.toString() == search.trim()
        }
        .take(5)

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader("Nova conta")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (option in listOf("Manual", "Open finance")) {
                    if (kind == option) {
                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                            onClick = { kind = option },
                        ) {
                            Text(option)
                        }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { kind = option },
                        ) {
                            Text(option)
                        }
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                singleLine = true,
                onValueChange = { name = it },
                label = { Text("Nome da conta") },
                shape = RoundedCornerShape(12.dp),
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = balance,
                singleLine = true,
                onValueChange = { balance = it },
                label = { Text("Saldo atual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = main, onCheckedChange = { main = it })
                Text("Conta principal", color = TextDark)
            }

            Text("Banco", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextDark)

            if (loadingBanks) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FincareBlue)
                }
            } else if (banksError) {
                Text("Não foi possível carregar os bancos", color = TextMuted)
            } else {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = search,
                    singleLine = true,
                    onValueChange = { search = it },
                    placeholder = { Text("Buscar por nome ou código") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    supportingText = { Text("${banksList.size} bancos carregados da BrasilAPI") },
                    shape = RoundedCornerShape(12.dp),
                )

                for (bank in results) {
                    val selected = selectedBank?.code == bank.code
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                if (selected) 2.dp else 1.dp,
                                if (selected) FincareBlue else Color(0xFFE6E8EC),
                                RoundedCornerShape(8.dp),
                            )
                            .clickable {
                                selectedBank = bank
                                if (name.isBlank()) {
                                    name = bank.name ?: ""
                                }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            bank.name ?: "-",
                            color = TextDark,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                        )
                        Text("${bank.code}", color = TextMuted, fontSize = 13.sp)
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                onClick = {
                    val parsedBalance = balance.replace(",", ".").toDoubleOrNull()
                    val bank = selectedBank
                    if (name.isBlank() || parsedBalance == null || bank == null) {
                        Toast.makeText(context, "Preencha os campos e escolha o banco", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    scope.launch {
                        val dao = DatabaseHelper.getInstance(context).accountDao()
                        if (main) {
                            dao.clearMain()
                        }
                        dao.insert(
                            Account(
                                name = name,
                                bankName = bank.fullName ?: bank.name ?: "",
                                kind = kind,
                                balance = parsedBalance,
                                main = main,
                            )
                        )
                        Toast.makeText(context, "Conta criada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
            ) {
                Text("Salvar conta")
            }
        }
    }
}
