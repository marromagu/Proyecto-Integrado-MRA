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
import com.proyecto.proyectointegradomra.ui.common.StandardButton



@Composable
fun StartScreen(
    navToSignUp: () -> Unit = {},
    navToLogIn: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Spacer(modifier = Modifier.weight(1f))
        StandardButton(
            text = "Iniciar sesión",
            icon = Icons.Filled.AccountCircle,
            onClick = navToLogIn
        )
        StandardButton(
            text = "Registrarse",
            icon = Icons.Filled.AccountBox,
            onClick = navToSignUp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}