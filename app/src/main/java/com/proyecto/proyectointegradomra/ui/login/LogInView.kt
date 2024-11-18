package com.proyecto.proyectointegradomra.ui.login

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
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoPorDefectoEditable
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto
import com.proyecto.proyectointegradomra.ui.common.Logo


@Composable
fun LogInView(
    logInController: LogInViewModel = viewModel(),
    dataRepository: DataRepository,
    navToHome: () -> Unit
) {
    val email by logInController.name.observeAsState("")
    val password by logInController.password.observeAsState("")

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

        CampoDeTextoPorDefectoEditable(
            label = "Correo Electrónico",
            value = email,
            icon = Icons.Filled.Email,
            onValueChange = { logInController.updateName(it) }
        )
        CampoDeTextoPorDefectoEditable(
            label = "Contraseña",
            value = password,
            onValueChange = { logInController.updatePassword(it) },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonPorDefecto(
            text = "Iniciar Sesión",
            icon = Icons.Filled.AccountCircle,
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Por favor, complete todos los campos"
                } else {
                    dataRepository.iniciarSesion(email, password, onSuccess = {
                        navToHome()
                    }, onError = {
                        errorMessage = "Error al iniciar sesión"
                    })
                }
            }
        )
    }
}