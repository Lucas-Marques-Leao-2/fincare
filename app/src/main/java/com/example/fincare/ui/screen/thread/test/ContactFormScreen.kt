package com.example.fincare.ui.screen
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.db.DatabaseHelper
import com.example.fincare.domain.Contact
import kotlinx.coroutines.launch


@Composable
fun ContactFormScreen() {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Surface(Modifier.padding(innerPadding)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 32.dp,
                        horizontal = 16.dp,
                    ), verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                Text(
                    "Cadastro de Contato",
                    style = MaterialTheme.typography.headlineMedium,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { value ->
                        name = value
                    },
                    label = { Text("Nome") },
                    shape = RoundedCornerShape(12.dp),
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = number,
                    singleLine = true,
                    onValueChange = { number = it },
                    label = { Text("Número") },
                    shape = RoundedCornerShape(12.dp),
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        scope.launch {
                            try {
                                DatabaseHelper
                                    .getInstance(context)
                                    .contactDao()
                                    .insert(
                                        Contact(
                                            number = number,
                                            name = name
                                        )
                                    )
                            } catch (e: Exception) {
                                Log.i("Erro", e.message.toString())
                                Toast.makeText(
                                    context,
                                    "Erro ao adicionar contato",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                ) {
                    Text("Cadastrar")
                }
            }
        }
    }
}


@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_3A,
)
@Composable
private fun PreviewRegisterScreen() {
    Surface(Modifier.padding(top = 64.dp)) {
        ContactFormScreen()
    }
}