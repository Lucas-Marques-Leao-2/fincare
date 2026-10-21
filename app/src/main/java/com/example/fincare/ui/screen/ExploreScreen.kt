package com.example.fincare.ui.screen

import android.util.Log
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
import com.example.fincare.sample.sampleDataAccommodation
import com.example.fincare.ui.components.CardAccommodation
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.example.fincare.api.RetrofitHelper
import com.example.fincare.domain.Accommodation
import com.example.fincare.domain.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

@Composable
fun ExploreScreen (navController: NavController) {

    var accommodationsList = remember { mutableStateListOf<Accommodation>() }


    val context = LocalContext.current

//    val scope = CoroutineScope(Dispatchers.IO)
//    val scope = rememberCoroutineScope()
//    scope.launch {
//        val accommodations = DatabaseHelper
//            .getInstance(context)
//            .accommodationDao()
//            .findAll()
//
//        accommodationsList.addAll(accommodations)
//        Toast.makeText(context, "Hospedagem carregada", Toast.LENGTH_SHORT)
//    }

    LaunchedEffect(Unit) {
        val accommodations = DatabaseHelper
            .getInstance(context)
            .accommodationDao()
            .findAll()

        accommodationsList.addAll(accommodations)
        Toast.makeText(context, "Hospedagem carregada", Toast.LENGTH_SHORT).show()

//        withContext(Dispatchers.IO) {
//            val call: Call<Address> =  RetrofitHelper().addressApi.findByCep("57314120")
//            val response: Response<Address> = call.execute()
//            Log.i("RESPONSE", "ExploreScreen: ${response.body()}")
//        }

        val address = RetrofitHelper().addressApi.findByCep("57314120")
        Log.i("RESPONSE", "ExploreScreen: $address")

    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(accommodationsList) { accommodation ->
                CardAccommodation(accommodation, navController = navController)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardAccommodationPreview(){
    CardAccommodation(sampleDataAccommodation,rememberNavController())
}