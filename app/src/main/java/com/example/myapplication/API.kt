package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET

interface API {
    @GET("serviceInstance?page=0&size=2000") //endpoint
    fun getCompanies(): Call<List<Service>>
}