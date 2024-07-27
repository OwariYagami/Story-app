package com.overdevx.mystoryapp.data.retrofit

import com.overdevx.mystoryapp.data.database.ResponseListStoryRoom
import com.overdevx.mystoryapp.data.response.ResponseListStory
import com.overdevx.mystoryapp.data.response.ResponseLogin
import com.overdevx.mystoryapp.data.response.ResponseRegister
import com.overdevx.mystoryapp.data.response.ResponseUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): ResponseUpload

    @GET("stories")
    suspend fun getListStoryWithLocation(
        @Query("location") location:Int = 1
    ):ResponseListStory
}