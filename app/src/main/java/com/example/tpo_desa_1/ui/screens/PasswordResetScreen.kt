package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.data.source.remote.ApiService
import com.example.tpo_desa_1.data.source.remote.ApiServiceBuilder
import com.example.tpo_desa_1.repository.PasswordResetRepository
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModel
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PasswordResetScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }

    // ViewModel + Factory manual
    val context = LocalContext.current
    val viewModel: PasswordResetViewModel = viewModel(
        factory = PasswordResetViewModelFactory(
            PasswordResetRepository(ApiServiceBuilder.apiService)
        )
    )

    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Recuperar contraseña",
                modifier = Modifier
                    .size(90.dp)
                    .padding(bottom = 16.dp),
                tint = Color(0xFF00A86B)
            )

            Text("¿Olvidaste tu contraseña?", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Vamos a enviar un código de 6 dígitos a tu correo.\nPor favor ingresá el mail con el que estás registrado:",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearError()
                },
                label = { Text("Mail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.requestReset(
                        email = email,
                        onSuccess = {
                            navController.navigate("verify_code/$email")
                        }
                    )
                },
                enabled = email.isNotBlank() && !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar código")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("← Regresar al inicio de sesión")
            }
        }
    }
}
