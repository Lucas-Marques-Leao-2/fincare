package com.example.fincare.api

import com.example.fincare.domain.Rate
import retrofit2.http.GET

interface RateApi {

    @GET("taxas/v1")
    suspend fun findAll(): List<Rate>
}
