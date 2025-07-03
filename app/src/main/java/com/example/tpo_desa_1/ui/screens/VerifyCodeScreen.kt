package com.example.tpo_desa_1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tpo_desa_1.R

@Composable
fun VerifyCodeScreen(
    navController: NavController,
    email: String
) {
    var codeInput by remember { mutableStateOf("") }
    var codigoIncorrecto by remember { mutableStateOf(false) }
    val expectedCode = "123456"

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

        Text("¡Por favor revisa tus mensajes!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Te enviamos un código de 6 dígitos para que verifiques tu identidad.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text( // <-- Muestra el email en gris
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
                    codigoIncorrecto = false // reset si cambia
                }
            },
            label = { Text("Código") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = codigoIncorrecto // 🔴 Borde rojo si es incorrecto
        )

        if (codigoIncorrecto) {
            Text(
                text = "Código incorrecto. Intenta nuevamente.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (codeInput == expectedCode) {
                    navController.navigate("new_password/$email") // ruta real
                } else {
                    codigoIncorrecto = true
                }
            },
            enabled = codeInput.length == 6,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar Código")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("¿Aún no recibiste el código? Reenviar código", fontSize = 12.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = {
            navController.popBackStack()
        }) {
            Text("⬅ Regresa al inicio de sesión")
        }
    }
}