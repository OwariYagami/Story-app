package com.overdevx.mystoryapp.data.repository

import androidx.lifecycle.viewModelScope
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.response.ResponseListStory
import com.overdevx.mystoryapp.data.response.ResponseLogin
import com.overdevx.mystoryapp.data.response.ResponseRegister
import com.overdevx.mystoryapp.data.response.ResponseUpload
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import com.overdevx.mystoryapp.data.retrofit.ApiServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(private val apiService: ApiServices,private val dataStoreManager: DataStoreManager) {

    suspend fun register(name: String, email: String, password: String): ResponseRegister {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): ResponseLogin {
        val response = apiService.login(email, password)
        if (response.loginResult != null && response.error == false) {
            dataStoreManager.saveLoginResult(response.loginResult)
        }
        return response
    }

    suspend fun getListStory(page: Int, size: Int): ResponseListStory {
        return apiService.getListStory(page, size)
    }

    fun getToken(): Flow<String?> {
        return dataStoreManager.userToken
    }

    suspend fun uploadImage(file: MultipartBody.Part, description: RequestBody): ResponseUpload {
        return apiService.uploadImage(file, description)
    }

    suspend fun logout(){
        return dataStoreManager.clearData()
    }
}