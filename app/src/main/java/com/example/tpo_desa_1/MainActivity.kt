package com.example.tpo_desa_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.navigation.AppNavigation
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Retrofit instance
        val apiService = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // ✅ UserPreferences (DataStore)
        val userPreferences = UserPreferences(applicationContext)

        // ✅ Nuevo repositorio sin SQLite
        val usuarioRepository = UsuarioRepository(apiService, userPreferences)

        // ✅ ViewModelFactory y ViewModel
        val sessionViewModelFactory = SessionViewModelFactory(application, usuarioRepository)
        val sessionViewModel = ViewModelProvider(this, sessionViewModelFactory)[SessionViewModel::class.java]

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppNavigation(sessionViewModel = sessionViewModel)
            }
        }
    }
}
