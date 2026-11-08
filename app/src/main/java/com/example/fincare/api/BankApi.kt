package com.example.fincare.api

import com.example.fincare.domain.Bank
import retrofit2.http.GET
import retrofit2.http.Path

interface BankApi {

    @GET("banks/v1/{code}")
    suspend fun findByCode(@Path("code") code: String): Bank
}
