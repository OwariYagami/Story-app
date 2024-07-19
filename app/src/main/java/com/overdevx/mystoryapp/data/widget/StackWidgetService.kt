package com.overdevx.mystoryapp.data.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.overdevx.mystoryapp.data.datastore.DataStoreManager
import com.overdevx.mystoryapp.data.repository.UserRepository
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val apiService = ApiConfig.getApiServicesWithToken(getTokenFromDataStore())
        val dataStoreManager = DataStoreManager(this.applicationContext)
        val userRepository = UserRepository(apiService, dataStoreManager)
        return StackRemoteViewsFactory(this.applicationContext, userRepository)
    }

    private fun getTokenFromDataStore(): String {
        return runBlocking {
            val dataStoreManager = DataStoreManager(this@StackWidgetService.applicationContext)
            dataStoreManager.userToken.firstOrNull() ?: ""
        }
    }
}