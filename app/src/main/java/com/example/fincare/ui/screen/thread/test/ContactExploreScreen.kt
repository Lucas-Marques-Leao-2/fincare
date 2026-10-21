package com.example.fincare.ui.screen.thread.test
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import com.example.fincare.domain.Contact
import com.example.fincare.sample.sampleDataContact
import com.example.fincare.sample.sampleDataContacts
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

//        DatabaseHelper.getInstance(context).contactDao().insertAll(sampleDataContacts)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(contactsList) { contact ->
                CardContact(contact, navController = navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardContactPreview(){
    CardContact(sampleDataContact,rememberNavController())
}