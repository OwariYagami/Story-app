package com.overdevx.mystoryapp.ui.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.di.Injection
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapsFragmentViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    fun fetchStoriesWithLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getListStoryWithLocation()
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
}
class MapsFragmentViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsFragmentViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

//    private fun createRepositoryWithToken(dataStoreManager: DataStoreManager): UserRepository {
//        var userRepository: UserRepository? = null
//        runBlocking {
//            val token = dataStoreManager.userToken.firstOrNull() ?: ""
//            val apiService = ApiConfig.getApiServicesWithToken(token)
//            userRepository = UserRepository(apiService, dataStoreManager)
//        }
//        return userRepository ?: throw IllegalStateException("UserRepository cannot be null")
//    }
}