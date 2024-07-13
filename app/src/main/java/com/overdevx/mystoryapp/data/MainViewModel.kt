package com.overdevx.mystoryapp.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overdevx.mystoryapp.auth.UserViewModel
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    fun fetchStories(page: Int, size: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getListStory(page, size)
                if (response.error == false && response.listStory != null) {
                    _stories.value = response.listStory.filterNotNull()
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("RETROFIT", "Gagal Login ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val dataStoreManager = DataStoreManager(context)
            val userRepository = createRepositoryWithToken(dataStoreManager)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userRepository) as T
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
