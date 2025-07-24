package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpo_desa_1.R
import com.example.tpo_desa_1.data.source.remote.ApiServiceBuilder
import com.example.tpo_desa_1.repository.PasswordResetRepository
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModel
import com.example.tpo_desa_1.viewmodel.PasswordResetViewModelFactory
import kotlinx.coroutines.delay
@Composable
fun VerifyCodeScreen(
    navController: NavController,
    email: String
) {
    var codeInput by remember { mutableStateOf("") }

    val viewModel: PasswordResetViewModel = viewModel(
        factory = PasswordResetViewModelFactory(
            PasswordResetRepository(ApiServiceBuilder.apiService)
        )
    )

    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Estado para el botón de reenviar
    var canResend by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableStateOf(30) }
    var resendCount by remember { mutableStateOf(0) }
    val maxResends = 3
    var resendTrigger by remember { mutableStateOf(0) } // para que LaunchedEffect se dispare con cada clic

    // Cooldown inicial automático
    LaunchedEffect(Unit) {
        canResend = false
        secondsRemaining = 30
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
        canResend = true
    }

    // Cooldown por clic en "Reenviar código"
    LaunchedEffect(resendTrigger) {
        if (resendTrigger == 0) return@LaunchedEffect
        canResend = false
        secondsRemaining = 30
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
        canResend = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("¡Por favor revisa tu correo!", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Te enviamos un código de 6 dígitos para que verifiques tu identidad.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Enviado a $email",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = codeInput,
            onValueChange = {
                if (it.length <= 6) {
                    codeInput = it
                    viewModel.clearError()
                }
            },
            label = { Text("Código") },
            singleLine = true,
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.verifyCode(
                    email = email,
                    totpCode = codeInput,
                    onSuccess = {
                        navController.navigate("new_password/$email/$codeInput")
                    }
                )
            },
            enabled = codeInput.length == 6 && !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Ingresar código")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔁 Botón "Reenviar código"
        OutlinedButton(
            onClick = {
                resendCount++
                resendTrigger++
                viewModel.requestReset(email) { /* sin navegación */ }
            },
            enabled = canResend && resendCount < maxResends,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                when {
                    resendCount >= maxResends -> "Reintentos agotados"
                    !canResend -> "Reenviar código (${secondsRemaining}s)"
                    else -> "Reenviar código"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.popBackStack()
        }) {
            Text("⬅ Regresar al inicio de sesión")
        }
    }
}