package com.overdevx.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.paging.StoryPagingSource
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.data.response.ResponseListStory
import com.overdevx.mystoryapp.data.response.ResponseLogin
import com.overdevx.mystoryapp.data.response.ResponseRegister
import com.overdevx.mystoryapp.data.response.ResponseUpload
import com.overdevx.mystoryapp.data.retrofit.ApiServices
import kotlinx.coroutines.flow.Flow
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

     fun getListStory():LiveData<PagingData<ListStoryItem>>  {
        return Pager(
            config = PagingConfig(5),
            pagingSourceFactory = {StoryPagingSource(apiService)}
        ).liveData
    }
    suspend fun getListStoryWidget(page: Int, size: Int): ResponseListStory {
        return apiService.getListStory(page, size)
    }
    suspend fun getListStoryWithLocation(): ResponseListStory {
        return apiService.getListStoryWithLocation()
    }

    fun getToken(): Flow<String?> {
        return dataStoreManager.userToken
    }

    fun getName(): Flow<String?>{
        return dataStoreManager.userName
    }

    suspend fun uploadImage(file: MultipartBody.Part, description: RequestBody): ResponseUpload {
        return apiService.uploadImage(file, description)
    }

    suspend fun logout(){
        return dataStoreManager.clearData()
    }
}