package com.proyecto.proyectointegradomra.view.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras
import com.proyecto.proyectointegradomra.ui.theme.ColorUnfocuseado
import com.proyecto.proyectointegradomra.ui.theme.VerdeClaro
import com.proyecto.proyectointegradomra.view.StandardField
import com.proyecto.proyectointegradomra.view.StandardButton
import com.proyecto.proyectointegradomra.view.Logo

@Composable
fun SingUpView(
    singUpController: SignUpController = viewModel(),
    authController: AuthController = viewModel(),
    navToHome: () -> Unit
) {
    val nombre by singUpController.nombre.observeAsState("")
    val correo by singUpController.correo.observeAsState("")
    val contrasena by singUpController.contrasena.observeAsState("")
    val repetirContrasena by singUpController.repetirContrasena.observeAsState("")
    val isChecked by singUpController.esOfertante.observeAsState(false)
    var errorMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Logo() }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            StandardField(label = "Nombre de usuario",
                value = nombre,
                icon = Icons.Filled.AccountBox,
                onValueChange = { singUpController.updateNombre(it) })
        }
        item {
            StandardField(
                label = "Correo electrónico",
                value = correo,
                icon = Icons.Filled.Email,
                onValueChange = { singUpController.updateCorreo(it) },
                keyboardType = KeyboardType.Email
            )
        }
        item {
            StandardField(
                label = "Contraseña",
                value = contrasena,
                onValueChange = { singUpController.updateContrasena(it) },
                isPassword = true
            )
        }
        item {
            StandardField(
                label = "Repita contraseña",
                value = repetirContrasena,
                onValueChange = { singUpController.updateRepetirContrasena(it) },
                isPassword = true
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isChecked,
                    onCheckedChange = { singUpController.updateEsOfertante(it) },
                    modifier = Modifier.size(32.dp),
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = VerdeClaro,
                        uncheckedColor = ColorUnfocuseado,
                        checkmarkColor = ColorDeLetras,
                        disabledCheckedColor = ColorUnfocuseado,
                        disabledUncheckedColor = ColorUnfocuseado,
                        disabledIndeterminateColor = ColorUnfocuseado

                    ),
                    interactionSource = remember { MutableInteractionSource() })
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Registrarse como Ofertante.",
                    style = TextStyle(
                        color = ColorDeLetras,
                    )
                )
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        if (errorMessage.isNotEmpty()) {
            item {
                Text(text = errorMessage, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        item {
            StandardButton(text = "Registrarse", icon = Icons.Filled.AccountBox, onClick = {
                if ((contrasena != repetirContrasena) || (contrasena.isEmpty())) {
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    authController.registrarse(correo, contrasena, nombre, isChecked, onSuccess = {
                        navToHome()
                    }, onError = { error -> errorMessage = error })
                }
            })
        }
    }
}
