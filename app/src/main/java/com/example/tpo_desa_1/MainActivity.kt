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
import com.example.tpo_desa_1.config.AuthInterceptor
import com.example.tpo_desa_1.data.db.RecetaDao
import com.example.tpo_desa_1.data.model.Receta
import com.example.tpo_desa_1.data.persistence.UserPreferences
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.data.source.remote.RecetaRemoteDataSource
import com.example.tpo_desa_1.data.source.local.RecetaLocalDataSource
import com.example.tpo_desa_1.navigation.AppNavigation
import com.example.tpo_desa_1.repository.RecetaRepositoryImpl
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.CrearRecetaViewModelFactory
import com.example.tpo_desa_1.viewmodel.CrearRecetaViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val apiService = Retrofit.Builder()
//            .baseUrl(AppConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//
//        val userPreferences = UserPreferences(applicationContext)

        val userPreferences = UserPreferences(applicationContext)
        val authInterceptor = AuthInterceptor(userPreferences)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        val apiService = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)


        // ✅ Nuevo repositorio sin SQLite
        val usuarioRepository = UsuarioRepository(apiService, userPreferences)

        // ✅ ViewModelFactory y ViewModel
        val sessionViewModelFactory = SessionViewModelFactory(application, usuarioRepository)
        val sessionViewModel = ViewModelProvider(this, sessionViewModelFactory)[SessionViewModel::class.java]

        val recetaRemoteDataSource = RecetaRemoteDataSource(apiService)
        val recetaLocalDataSource = RecetaLocalDataSource(object : RecetaDao {
            override suspend fun obtenerTodas(): List<Receta> = emptyList()
            override suspend fun obtenerPorId(id: Int): Receta? = null
            override suspend fun insertar(receta: Receta) {}
            override suspend fun insertarTodas(recetas: List<Receta>) {}
            override suspend fun obtenerRecetasPorUsuario(alias: String): List<Receta> = emptyList()
            override suspend fun obtenerRecientesAprobadas(): List<Receta> = emptyList()
            override suspend fun obtenerAprobadas(): List<Receta> = emptyList()
            override suspend fun borrarTodas() {}
            override suspend fun eliminar(receta: Receta) {}
            override suspend fun obtenerPorNombre(nombre: String): Receta? = null
        }) // tengo q tenerlo a mano hasta q se integre y se use una receta real
        val recetaRepository = RecetaRepositoryImpl(recetaLocalDataSource, recetaRemoteDataSource)
        val crearRecetaViewModelFactory = CrearRecetaViewModelFactory(
            recetaRepository,
            userPreferences,
            applicationContext
        )
        val crearRecetaViewModel = ViewModelProvider(this, crearRecetaViewModelFactory)[CrearRecetaViewModel::class.java]

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppNavigation(
                    sessionViewModel = sessionViewModel,
                    crearRecetaViewModel = crearRecetaViewModel,
                    apiService = apiService
                )
            }
        }
    }
}
