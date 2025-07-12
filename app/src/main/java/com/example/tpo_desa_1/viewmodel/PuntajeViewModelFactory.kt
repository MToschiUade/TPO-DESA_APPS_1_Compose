// PuntajeViewModelFactory.kt
package com.example.tpo_desa_1.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.data.source.remote.ComentarioRemoteDataSource
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.repository.RecetaRepositoryImpl
import com.example.tpo_desa_1.config.AppConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PuntajeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuntajeViewModel::class.java)) {

            val db = AppDatabase.getDatabase(context)

            val localDataSource = RecetaLocalDataSource(
                recetaDao = db.recetaDao(),
                pasoDao = db.pasoRecetaDao(),
                ingredienteDao = db.ingredienteDao()
            )

            val retrofit = Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val recetaRemote = RecetaRemoteDataSource(apiService)
            val comentarioRemote = ComentarioRemoteDataSource(apiService)

            val repository = RecetaRepositoryImpl(localDataSource, recetaRemote, comentarioRemote)

            @Suppress("UNCHECKED_CAST")
            return PuntajeViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
