package com.overdevx.mystoryapp.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiServicesWithToken(token:String):ApiServices{
        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                    return chain.proceed(newRequest)
                }
            })
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5,TimeUnit.MINUTES)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiServices::class.java)
    }
}