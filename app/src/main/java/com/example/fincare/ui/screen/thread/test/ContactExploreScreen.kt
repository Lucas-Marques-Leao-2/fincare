package com.example.fincare.ui.screen.thread.test
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fincare.db.DatabaseHelper
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import com.example.fincare.domain.Contact
import com.example.fincare.sample.sampleDataContact
import com.example.fincare.ui.components.CardContact

@Composable
fun ContactExploreScreen (navController: NavController) {

    var contactsList = remember { mutableStateListOf<Contact>() }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val contacts = DatabaseHelper
            .getInstance(context)
            .contactDao()
            .findAll()

        contactsList.addAll(contacts)
        Toast.makeText(context, "Contatos carregados", Toast.LENGTH_SHORT).show()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(contactsList) { contact ->
                    CardContact(contact)
                }
            }

            Row (horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    navController.navigate("detailScreen")
                }) {
                    Text(
                        "Cadastro de Contato",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardContactPreview(){
    CardContact(sampleDataContact)
}