package com.example.fincare.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.ui.components.FincareLogo
import com.example.fincare.ui.theme.FincareBlue
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(navController: NavController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("New here? ", color = Color(0xFF8A8A8A))
            TextButton(
                onClick = { navController.navigate("signUpScreen") },
                contentPadding = PaddingValues(0.dp),
            ) {
                Text("Create an Account", color = FincareBlue)
            }
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Email", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                placeholder = { Text("Enter email") },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
            )
        }

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Password", fontWeight = FontWeight.Medium)
                TextButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Password recovery is not available",
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text("Forgot Password?", color = FincareBlue, fontSize = 13.sp)
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FincareBlue),
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Fill in email and password", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val user = DatabaseHelper
                            .getInstance(context)
                            .userDao()
                            .findByEmail(email.trim())

                        if (user == null || user.password != password) {
                            Toast.makeText(
                                context,
                                "Invalid email or password",
                                Toast.LENGTH_SHORT,
                            ).show()
                            return@launch
                        }

                        navController.navigate("homeScreen") {
                            popUpTo("signInScreen") { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Could not sign in", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        ) {
            Text("Sign In")
        }

        Text(
            text = "Or sign in with:",
            color = Color(0xFF8A8A8A),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SocialButton(
                label = "G",
                background = Color.White,
                contentColor = Color(0xFF4285F4),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                onClick = { socialToast(context) },
            )
            SocialButton(
                label = "f",
                background = Color(0xFF1877F2),
                contentColor = Color.White,
                border = null,
                onClick = { socialToast(context) },
            )
            SocialButton(
                label = "X",
                background = Color.Black,
                contentColor = Color.White,
                border = null,
                onClick = { socialToast(context) },
            )
        }
    }
}

@Composable
private fun SocialButton(
    label: String,
    background: Color,
    contentColor: Color,
    border: BorderStroke?,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        border = border,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = background),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, color = contentColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = Color(0xFFF3F4F6),
    focusedContainerColor = Color(0xFFF3F4F6),
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = FincareBlue,
)

private fun socialToast(context: android.content.Context) {
    Toast.makeText(context, "Use email to sign in", Toast.LENGTH_SHORT).show()
}
