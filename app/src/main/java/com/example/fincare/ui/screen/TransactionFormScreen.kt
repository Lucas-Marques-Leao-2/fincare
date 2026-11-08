package com.example.fincare.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.Transaction
import com.example.fincare.ui.theme.FincareBlue
import kotlinx.coroutines.launch

@Composable
fun TransactionFormScreen(navController: NavController) {
    val context = LocalContext.current

    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Despesa") }
    var date by remember { mutableStateOf("") }
    var bankCode by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Surface(Modifier.padding(innerPadding)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Registrar transação",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (type == "Despesa") {
                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                            onClick = { type = "Despesa" },
                        ) {
                            Text("Despesa")
                        }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { type = "Despesa" },
                        ) {
                            Text("Despesa")
                        }
                    }

                    if (type == "Receita") {
                        Button(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                            onClick = { type = "Receita" },
                        ) {
                            Text("Receita")
                        }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            onClick = { type = "Receita" },
                        ) {
                            Text("Receita")
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amount,
                    singleLine = true,
                    onValueChange = { amount = it },
                    label = { Text("Valor") },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = description,
                    singleLine = true,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = category,
                    singleLine = true,
                    onValueChange = { category = it },
                    label = { Text("Categoria") },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = date,
                    singleLine = true,
                    onValueChange = { date = it },
                    label = { Text("Data") },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = bankCode,
                    singleLine = true,
                    onValueChange = { bankCode = it },
                    label = { Text("Código do banco") },
                    supportingText = { Text("Ex.: 260 Nubank, 1 Banco do Brasil") },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (bankCode.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Informe o código do banco",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                return@IconButton
                            }
                            scope.launch {
                                try {
                                    val bank = RetrofitHelper().bankApi.findByCode(bankCode.trim())
                                    bankName = bank.fullName ?: bank.name ?: ""
                                    if (bankName.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Banco não encontrado",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    bankName = ""
                                    Toast.makeText(
                                        context,
                                        "Banco não encontrado",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        }) {
                            Icon(Icons.Outlined.Search, contentDescription = "Buscar banco")
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = bankName,
                    singleLine = true,
                    onValueChange = { bankName = it },
                    label = { Text("Banco") },
                    shape = RoundedCornerShape(12.dp),
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
                    onClick = {
                        val parsedAmount = amount.replace(",", ".").toDoubleOrNull()
                        if (
                            description.isBlank() ||
                            category.isBlank() ||
                            date.isBlank() ||
                            bankName.isBlank() ||
                            parsedAmount == null
                        ) {
                            Toast.makeText(
                                context,
                                "Preencha os campos e busque o banco",
                                Toast.LENGTH_SHORT,
                            ).show()
                            return@Button
                        }

                        scope.launch {
                            try {
                                DatabaseHelper
                                    .getInstance(context)
                                    .transactionDao()
                                    .insert(
                                        Transaction(
                                            description = description,
                                            category = category,
                                            amount = parsedAmount,
                                            type = type,
                                            date = date,
                                            bankCode = bankCode,
                                            bankName = bankName,
                                        )
                                    )

                                navController.popBackStack()
                            } catch (e: Exception) {
                                Log.i("Erro", e.message.toString())
                                Toast.makeText(
                                    context,
                                    "Erro ao registrar transação",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                    },
                ) {
                    Text("Registrar")
                }
            }
        }
    }
}
