package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.repository.RecetaRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {

            val recetaDao = AppDatabase.getDatabase(context).recetaDao()
            val localDataSource = RecetaLocalDataSource(recetaDao)

            val apiService = Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            val remoteDataSource = RecetaRemoteDataSource(apiService)

            val repository: RecetaRepository = RecetaRepositoryImpl(
                localDataSource,
                remoteDataSource
            )

            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
