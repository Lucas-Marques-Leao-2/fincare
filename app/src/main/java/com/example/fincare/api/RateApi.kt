package com.example.fincare.api

import com.example.fincare.domain.Rate
import retrofit2.http.GET
import retrofit2.http.Path

interface RateApi {

    @GET("taxas/v1")
    suspend fun findAll(): List<Rate>

    @GET("taxas/v1/{sigla}")
    suspend fun findBySigla(@Path("sigla") sigla: String): Rate
}
