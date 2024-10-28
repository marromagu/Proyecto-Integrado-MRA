package com.proyecto.proyectointegradomra.view.singUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.StandardField
import com.proyecto.proyectointegradomra.view.StandardButton
import com.proyecto.proyectointegradomra.view.Logo

@Composable
fun SingUpView(
    singUpController: SingUpController = viewModel(),
    authController: AuthController = viewModel(),
    navToHome: NavHostController
) {
    val nombre by singUpController.nombre.observeAsState("")
    val correo by singUpController.correo.observeAsState("")
    val contrasena by singUpController.contrasena.observeAsState("")
    val repetirContrasena by singUpController.repetirContrasena.observeAsState("")

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
            label = "Nombre de usuario",
            value = nombre,
            icon = Icons.Filled.AccountBox,
            onValueChange = { singUpController.updateNombre(it) }
        )
        StandardField(
            label = "Correo electrónico",
            value = correo,
            icon = Icons.Filled.Email,
            onValueChange = { singUpController.updateCorreo(it) },
            keyboardType = KeyboardType.Email
        )
        StandardField(
            label = "Contraseña",
            value = contrasena,
            onValueChange = { singUpController.updateContrasena(it) },
            isPassword = true
        )
        StandardField(
            label = "Repita contraseña",
            value = repetirContrasena,
            onValueChange = { singUpController.updateRepetirContrasena(it) },
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        StandardButton(
            text = "Registrarse",
            icon = Icons.Filled.AccountBox,
            onClick = {
                if ((contrasena != repetirContrasena) || (contrasena.isEmpty())) {
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    authController.registrarse(correo, contrasena, nombre, onSuccess = {
                        navToHome.navigate("HomeView")
                    }, onError = { error -> errorMessage = error })
                }
            }
        )
    }
}
