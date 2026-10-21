package com.example.fincare.api

import com.example.fincare.domain.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressApi {

//    @GET("/cep/v2/{cep}")
//    fun findByCep(@Path("cep") cep: String): Call<Address>

    @GET("/cep/v2/{cep}")
    suspend fun findByCep(@Path("cep") cep: String): Address
}