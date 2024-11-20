package com.proyecto.proyectointegradomra.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.common.Logo
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto

@Composable
fun StartView(
    navToSignUp: () -> Unit = {},
    navToLogIn: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(.5f))
        Logo()
        Spacer(modifier = Modifier.weight(1f))

        // Botón para navegar a la pantalla de inicio de sesión
        BotonPorDefecto(
            text = "Iniciar sesión",
            icon = Icons.Filled.AccountCircle,
            onClick = navToLogIn
        )

        // Botón para navegar a la pantalla de registro
        BotonPorDefecto(
            text = "Registrarse",
            icon = Icons.Filled.AccountBox,
            onClick = navToSignUp
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}