package com.proyecto.proyectointegradomra.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.StandardField
import com.proyecto.proyectointegradomra.view.StandardButton
import com.proyecto.proyectointegradomra.view.Logo


@Composable
fun LogInView(
    logInController: LogInController = viewModel(),
    authController: AuthController = viewModel(),
    navToHome: () -> Unit
) {
    val correo by logInController.nombre.observeAsState("")
    val contrasena by logInController.contrasena.observeAsState("")

    var errorMessage by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()

        Spacer(modifier = Modifier.height(24.dp))

        StandardField(
            label = "Correo Electrónico",
            value = correo,
            icon = Icons.Filled.Email,
            onValueChange = { logInController.updateNombre(it) }
        )
        StandardField(
            label = "Contraseña",
            value = contrasena,
            onValueChange = { logInController.updateContrasena(it) },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        StandardButton(
            text = "Iniciar Sesión",
            icon = Icons.Filled.AccountCircle,
            onClick = {
                if (correo.isEmpty() || contrasena.isEmpty()) {
                    errorMessage = "Por favor, complete todos los campos"
                } else {
                    authController.iniciarSesion(correo, contrasena, onSuccess = {
                        navToHome()
                    }, onError = { error -> errorMessage = error })
                }
            }
        )
    }
}