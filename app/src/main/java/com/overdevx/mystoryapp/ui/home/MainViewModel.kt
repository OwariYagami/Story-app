package com.overdevx.mystoryapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.overdevx.mystoryapp.data.database.ListStoryItemRoom
import com.overdevx.mystoryapp.data.database.StoryDatabase
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.di.Injection
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ListStoryItem
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val stories: LiveData<PagingData<ListStoryItemRoom>> = userRepository.getListStory().cachedIn(viewModelScope)
    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

//    fun fetchStories(page: Int, size: Int) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = userRepository.getListStory(page, size)
//                if (response.error == false && response.listStory != null) {
//                    _stories.value = response.listStory.filterNotNull()
//                    _isLoading.value = false
//                }
//            } catch (e: Exception) {
//                Log.e("RETROFIT", "Gagal Login ${e.message}")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getName(){
        viewModelScope.launch {
           userRepository.getName().collect{name->
               _userName.value=name
           }
        }
    }
}

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

//    private fun createRepositoryWithToken(dataStoreManager: DataStoreManager, database: StoryDatabase): UserRepository {
//        var userRepository: UserRepository? = null
//        runBlocking {
//            val token = dataStoreManager.userToken.firstOrNull() ?: ""
//            val apiService = ApiConfig.getApiServicesWithToken(token)
//            userRepository = UserRepository(database, apiService,dataStoreManager)
//        }
//        return userRepository ?: throw IllegalStateException("UserRepository cannot be null")
//    }
}
