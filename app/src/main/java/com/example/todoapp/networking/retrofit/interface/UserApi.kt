package com.example.todoapp.networking.retrofit.`interface`

import com.example.todoapp.networking.retrofit.model.MyName
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi {

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part photo: MultipartBody.Part,
    ): Call<MyName>


}