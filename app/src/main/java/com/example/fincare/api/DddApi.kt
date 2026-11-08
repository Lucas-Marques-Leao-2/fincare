package com.example.fincare.api

import com.example.fincare.domain.Ddd
import retrofit2.http.GET
import retrofit2.http.Path

interface DddApi {

    @GET("ddd/v1/{ddd}")
    suspend fun findByDdd(@Path("ddd") ddd: String): Ddd
}
