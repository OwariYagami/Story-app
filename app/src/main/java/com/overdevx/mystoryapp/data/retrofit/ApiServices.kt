package com.overdevx.mystoryapp.data.retrofit

import com.overdevx.mystoryapp.data.response.ResponseListStory
import com.overdevx.mystoryapp.data.response.ResponseLogin
import com.overdevx.mystoryapp.data.response.ResponseRegister
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @GET("stories")
    suspend fun getListStory(
        @Query("page") page:Int,
        @Query("size") size:Int
    ):ResponseListStory
}