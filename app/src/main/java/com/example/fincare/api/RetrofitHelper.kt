package com.example.fincare.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    private val client = Retrofit.Builder()
        .baseUrl("https://brasilapi.com.br/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val addressApi = client.create(AddressApi::class.java)

}