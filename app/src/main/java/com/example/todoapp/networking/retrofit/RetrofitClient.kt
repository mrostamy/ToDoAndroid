package com.example.todoapp.networking.retrofit

import com.example.todoapp.networking.retrofit.`interface`.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private var userAPi: UserApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.104:8000/api/users/")//android emulator
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userAPi = retrofit.create(UserApi::class.java)
    }

    companion object {

        private var instance: RetrofitClient? = null

        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }

            return instance as RetrofitClient
        }
    }

    fun getApi(): UserApi {
        return userAPi
    }


}