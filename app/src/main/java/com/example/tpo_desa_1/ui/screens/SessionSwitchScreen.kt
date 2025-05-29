package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.ui.components.ScreenWithBottomBar
import com.example.tpo_desa_1.viewmodel.SessionViewModel
import androidx.compose.ui.graphics.Color


@Composable
fun SessionSwitchScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    val usuario by sessionViewModel.usuarioLogueado
    var identificador by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorLogin by remember { mutableStateOf(false) }

    ScreenWithBottomBar(navController = navController) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (usuario != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Bienvenido ${usuario?.alias}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { sessionViewModel.logout() }) {
                        Text("Cerrar sesión")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Para esta actividad debe estar loggueado",
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = identificador,
                        onValueChange = { identificador = it },
                        label = { Text("Alias o Email") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        sessionViewModel.login(identificador, password) { success ->
                            errorLogin = !success
                        }
                    }) {
                        Text("Iniciar sesión")
                    }

                    if (errorLogin) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Credenciales incorrectas", color = Color.Red)
                    }
                }
            }
        }
    }
}
