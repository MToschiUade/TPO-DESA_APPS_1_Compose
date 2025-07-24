package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.data.source.remote.ApiServiceBuilder
import com.example.tpo_desa_1.repository.PasswordResetRepository
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModel
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun NewPasswordScreen(
    navController: NavController,
    email: String,
    totpCode: String
) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val viewModel: PasswordResetViewModel = viewModel(
        factory = PasswordResetViewModelFactory(
            PasswordResetRepository(ApiServiceBuilder.apiService)
        )
    )

    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val passwordsMatch = password == repeatPassword && password.length >= 6

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )

                Text("Restablece tu contraseña", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ingresá tu nueva contraseña dos veces", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        viewModel.clearError()
                    },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Ver contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = {
                        repeatPassword = it
                        viewModel.clearError()
                    },
                    label = { Text("Repetir contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Ver contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage ?: "", color = Color.Red, fontSize = 12.sp)
                } else if (!passwordsMatch && password.isNotBlank() && repeatPassword.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Las contraseñas no coinciden o son muy cortas", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.changePassword(
                            email = email,
                            totpCode = totpCode,
                            newpass = password,
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("✅ Contraseña actualizada con éxito. ¡Ahora podés iniciar sesión!")
                                    delay(2500)
                                    navController.navigate("home") {
                                        popUpTo("session_switch") { inclusive = true }
                                    }
                                }
                            }
                        )
                    },
                    enabled = passwordsMatch && !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Cambiar contraseña")
                    }
                }
            }
        }
    }
}


