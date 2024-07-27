package com.overdevx.mystoryapp.data.di

import android.content.Context
import com.overdevx.mystoryapp.data.database.StoryDatabase
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = StoryDatabase.getDatabase(context)
        val dataStoreManager = DataStoreManager(context)
        val token = runBlocking { dataStoreManager.userToken.firstOrNull() ?: "" }
        val apiService = ApiConfig.getApiServicesWithToken(token)
        return UserRepository(database, apiService, dataStoreManager)
    }
}