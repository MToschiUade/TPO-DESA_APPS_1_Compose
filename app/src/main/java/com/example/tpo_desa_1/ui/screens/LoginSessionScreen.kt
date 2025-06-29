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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.navigation.Screen
import com.example.tpo_desa_1.viewmodel.LoginResult
import com.example.tpo_desa_1.viewmodel.SessionViewModel

@Composable
fun LoginSessionScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    var identificador by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorLogin by remember { mutableStateOf(false) }

    val camposValidos = identificador.isNotBlank() && password.length >= 6
    val loginState by sessionViewModel.loginState

    // Navegar o mostrar errores en base al estado del login
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginResult.Success -> {
                errorLogin = false
                navController.navigate(Screen.Splash.route) {
                    popUpTo(Screen.SessionSwitch.route) { inclusive = true }
                    launchSingleTop = true
                }
            }

            is LoginResult.Error -> {
                errorLogin = true
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Botón cerrar
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp,
                    end = 16.dp
                )
        ) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Black)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            Text("Ratatouille", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Inicia sesión en tu cuenta", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = identificador,
                onValueChange = { identificador = it },
                placeholder = { Text("mail@mail.com") },
                label = { Text("Alias") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Contraseña", style = MaterialTheme.typography.labelSmall)
                Text(
                    "¿Olvidaste tu contraseña?",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("6+ characters") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(icon, contentDescription = null)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    errorLogin = false
                    sessionViewModel.login(identificador, password) {}
                },
                enabled = camposValidos && loginState !is LoginResult.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.LightGray
                )
            ) {
                if (loginState is LoginResult.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Ingresar")
                }
            }

            if (errorLogin) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Credenciales incorrectas", color = Color.Red, fontSize = 12.sp)
            }
        }
    }
}
