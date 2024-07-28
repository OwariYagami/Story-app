package com.overdevx.mystoryapp.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.overdevx.mystoryapp.data.database.ListStoryItemRoom
import com.overdevx.mystoryapp.data.di.Injection
import com.overdevx.mystoryapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val stories: LiveData<PagingData<ListStoryItemRoom>> = userRepository.getListStory().cachedIn(viewModelScope)
    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

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

}
