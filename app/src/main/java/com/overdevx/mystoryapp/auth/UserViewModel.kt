package com.overdevx.mystoryapp.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.di.Injection
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.response.ResponseError
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

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> = _loginError

    private val _registerError = MutableLiveData<String>()
    val registerError: LiveData<String> = _registerError

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    fun register(name: String, email: String, password: String) {
        _isLoading.value=true
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                _registerResult.value = response
            } catch (e: Exception) {
                val errorMessage = if (e is retrofit2.HttpException) {
                    try {
                        val jsonInString = e.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, ResponseError::class.java)
                        errorBody.message
                    } catch (jsonException: Exception) {
                        "Unknown error occurred"
                    }
                } else {
                    "Login failed: ${e.message}"
                }
                Log.e("RETROFIT", "Login Failed $errorMessage")
                _registerError.value = "$errorMessage"
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
                val errorMessage = if (e is retrofit2.HttpException) {
                    try {
                        val jsonInString = e.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, ResponseError::class.java)
                        errorBody.message
                    } catch (jsonException: Exception) {
                        "Unknown error occurred"
                    }
                } else {
                    "Login failed: ${e.message}"
                }
                Log.e("RETROFIT", "Login failed $errorMessage")
                _loginError.value ="$errorMessage"
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
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

