package com.overdevx.mystoryapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.overdevx.mystoryapp.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val USER_TOKEN_KEY = stringPreferencesKey("USER_TOKEN")
        val USER_ID_KEY = stringPreferencesKey("USER_ID")
        val USER_NAME_KEY = stringPreferencesKey("USER_NAME")
    }

    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_TOKEN_KEY]
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }
    val userName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]
        }

    suspend fun saveLoginResult(loginResult: LoginResult) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = loginResult.token ?: ""
            preferences[USER_NAME_KEY] = loginResult.name ?: ""
            preferences[USER_ID_KEY] = loginResult.userId ?: ""
        }
    }

    suspend fun clearData() {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = ""
        }
    }

}
