package com.example.fincare.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.User
import com.example.fincare.ui.components.FincareLogo
import com.example.fincare.ui.theme.FincareBlue
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        FincareLogo()

        Text(
            text = "Criar conta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Já tem uma conta? ", color = Color(0xFF8A8A8A))
            TextButton(
                onClick = { navController.popBackStack() },
                contentPadding = PaddingValues(0.dp),
            ) {
                Text("Entrar", color = FincareBlue)
            }
        }

        LabeledField(label = "Nome", value = name, onValueChange = { name = it }, placeholder = "Digite seu nome completo")
        LabeledField(label = "Email", value = email, onValueChange = { email = it }, placeholder = "Digite seu email")

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Telefone", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = { phone = it },
                singleLine = true,
                placeholder = { Text("(99) 9 9999-9999") },
                prefix = {
                    Text("+55  ", color = FincareBlue, fontWeight = FontWeight.SemiBold)
                },
                leadingIcon = { Text("🇧🇷") },
                shape = RoundedCornerShape(8.dp),
                colors = signUpFieldColors(),
            )
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Senha", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = signUpFieldColors(),
            )
            LinearProgressIndicator(
                progress = { passwordStrength(password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color(0xFF43A047),
                trackColor = Color(0xFFE6E6E6),
            )
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Confirmar senha", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = signUpFieldColors(),
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
            onClick = {
                if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (password != confirmPassword) {
                    Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val ddd = dddFromPhone(phone)
                if (ddd.length < 2) {
                    Toast.makeText(context, "Informe o DDD no telefone", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val info = RetrofitHelper().dddApi.findByDdd(ddd)
                        val state = info.state
                        if (state.isNullOrBlank()) {
                            Toast.makeText(context, "DDD não encontrado", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val dao = DatabaseHelper.getInstance(context).userDao()
                        if (dao.findByEmail(email.trim()) != null) {
                            Toast.makeText(context, "Email já cadastrado", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        dao.insert(
                            User(
                                name = name.trim(),
                                email = email.trim(),
                                phone = phone.trim(),
                                password = password,
                                state = state,
                            )
                        )

                        Toast.makeText(context, "Conta criada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } catch (e: Exception) {
                        Log.i("Erro", e.message.toString())
                        Toast.makeText(context, "DDD não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        ) {
            Text("Criar conta")
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(8.dp),
            colors = signUpFieldColors(),
        )
    }
}

@Composable
private fun signUpFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFFF3F4F6),
    focusedContainerColor = Color(0xFFF3F4F6),
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = FincareBlue,
)

private fun passwordStrength(password: String): Float {
    if (password.isEmpty()) return 0f
    var score = 0
    if (password.length >= 6) score++
    if (password.length >= 10) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return score / 4f
}

private fun dddFromPhone(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    val local = if (digits.startsWith("55") && digits.length > 11) digits.drop(2) else digits
    return local.take(2)
}
