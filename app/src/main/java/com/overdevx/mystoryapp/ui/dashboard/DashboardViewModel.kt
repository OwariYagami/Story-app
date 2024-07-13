package com.overdevx.mystoryapp.ui.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overdevx.mystoryapp.data.MainViewModel
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ResponseUpload
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DashboardViewModel (private val userRepository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<ResponseUpload?>()
    val uploadResult: MutableLiveData<ResponseUpload?> = _uploadResult

    fun uploadImage(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = userRepository.uploadImage(file, description)
                _uploadResult.postValue(response)
                _isLoading.value = false
            } catch (e: Exception) {
                // Handle error
                Log.e("UserViewModel", "uploadImage: ${e.message}", e)
                _isLoading.value = false
            }finally {

            }
        }
    }
}
class DashboardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            val dataStoreManager = DataStoreManager(context)
            val userRepository = createRepositoryWithToken(dataStoreManager)
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createRepositoryWithToken(dataStoreManager: DataStoreManager): UserRepository {
        // Create a coroutine scope for the factory
        val scope = CoroutineScope(Dispatchers.IO)
        var userRepository: UserRepository? = null

        // Run a blocking coroutine to wait for the token
        runBlocking {
            val token = dataStoreManager.userToken.firstOrNull() ?: ""
            val apiService = ApiConfig.getApiServicesWithToken(token)
            userRepository = UserRepository(apiService, dataStoreManager)
        }
        return userRepository ?: throw IllegalStateException("UserRepository cannot be null")
    }
}