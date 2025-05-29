package com.example.tpo_desa_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.tpo_desa_1.data.db.AppDatabase
import com.example.tpo_desa_1.navigation.AppNavigation
import com.example.tpo_desa_1.repository.UsuarioRepository
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import com.example.tpo_desa_1.viewmodel.SessionViewModelFactory

//import com.example.tpo_desa_1.ui.theme.Tpo_desa_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usuarioDao = AppDatabase.getDatabase(applicationContext).usuarioDao()
        val usuarioRepository = UsuarioRepository(usuarioDao)
        val sessionViewModelFactory = SessionViewModelFactory(usuarioRepository)
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

