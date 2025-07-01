package com.example.tpo_desa_1.data.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Usamos extension global para evitar instancias separadas
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ALIAS = stringPreferencesKey("alias")
        val EMAIL = stringPreferencesKey("email")
    }

    suspend fun saveLoginData(alias: String, email: String, accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[ALIAS] = alias
            prefs[EMAIL] = email
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
        println("🧼 Preferences limpiadas correctamente")
    }

    // Exponer flujos reactivos para SessionViewModel
    fun getAccessToken(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[ACCESS_TOKEN] }

    fun getRefreshToken(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[REFRESH_TOKEN] }

    fun getAlias(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[ALIAS] }

    fun getEmail(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[EMAIL] }

    fun isLoggedIn(): Flow<Boolean> =
        getAccessToken().map { !it.isNullOrBlank() }
}

