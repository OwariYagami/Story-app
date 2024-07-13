package com.overdevx.mystoryapp.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ResponseLogin
import com.overdevx.mystoryapp.data.response.ResponseRegister
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResponseRegister>()
    val registerResult: LiveData<ResponseRegister> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<ResponseLogin>()
    val loginResult: LiveData<ResponseLogin> get() = _loginResult

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    fun register(name: String, email: String, password: String) {
        _isLoading.value=true
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                _registerResult.value = response
            } catch (e: Exception) {
                Log.e("RETROFIT", "Gagal Login ${e.message}")
            } finally {
                _isLoading.value=false
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value=true
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, password)
                _loginResult.value = response
            } catch (e: Exception) {
                Log.e("RETROFIT", "Gagal Login ${e.message}")
            }finally {
                _isLoading.value=false
            }
        }
    }

     fun fetchToken() {
        viewModelScope.launch {
            userRepository.getToken().collect { token ->
                _token.value = token
            }
        }
    }
}
class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            val apiService = ApiConfig.getApiServicesWithToken("")
            val dataStoreManager = DataStoreManager(context)
            val userRepository = UserRepository(apiService, dataStoreManager)
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

