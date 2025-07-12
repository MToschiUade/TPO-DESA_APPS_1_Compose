// ComentarioViewModelFactory.kt
package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.config.AppConfig
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.data.source.remote.ComentarioRemoteDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.repository.RecetaRepository
import com.example.tpo_desa_1.repository.RecetaRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ComentarioViewModelFactory(
    private val context: Context,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComentarioViewModel::class.java)) {

            val db = AppDatabase.getDatabase(context)

            val localDataSource = RecetaLocalDataSource(
                recetaDao = db.recetaDao(),
                pasoDao = db.pasoRecetaDao(),
                ingredienteDao = db.ingredienteDao()
            )

            val apiService = Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            val remoteDataSource = RecetaRemoteDataSource(apiService)

            val comentarioRemoteDataSource = ComentarioRemoteDataSource(apiService)

            val repository: RecetaRepository = RecetaRepositoryImpl(
                localDataSource = localDataSource,
                remoteDataSource = remoteDataSource,
                comentarioRemoteDataSource = comentarioRemoteDataSource
            )


            @Suppress("UNCHECKED_CAST")
            return ComentarioViewModel(repository, userPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
