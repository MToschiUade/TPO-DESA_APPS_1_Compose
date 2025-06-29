package com.example.tpo_desa_1.data.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(context: Context) {

    private val appContext = context.applicationContext

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ALIAS = stringPreferencesKey("alias")
        val EMAIL = stringPreferencesKey("email")
    }

    suspend fun saveLoginData(alias: String, email: String, accessToken: String, refreshToken: String) {
        appContext.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[ALIAS] = alias
            prefs[EMAIL] = email
        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { it.clear() }
        println("🧼 Preferences limpiadas desde UserPreferences.clear()")
    }


    val accessTokenFlow: Flow<String?> = appContext.dataStore.data.map { it[ACCESS_TOKEN] }
    val refreshTokenFlow: Flow<String?> = appContext.dataStore.data.map { it[REFRESH_TOKEN] }
    val aliasFlow: Flow<String?> = appContext.dataStore.data.map { it[ALIAS] }
    val emailFlow: Flow<String?> = appContext.dataStore.data.map { it[EMAIL] }

    val isLoggedIn: Flow<Boolean> = accessTokenFlow.map { !it.isNullOrBlank() }
}
