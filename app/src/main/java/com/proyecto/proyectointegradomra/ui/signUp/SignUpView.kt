package com.proyecto.proyectointegradomra.ui.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras
import com.proyecto.proyectointegradomra.ui.theme.ColorUnfocuseado
import com.proyecto.proyectointegradomra.ui.theme.VerdeClaro
import com.proyecto.proyectointegradomra.ui.common.CampoDeTextoPorDefectoEditable
import com.proyecto.proyectointegradomra.ui.common.BotonPorDefecto
import com.proyecto.proyectointegradomra.ui.common.Logo
import com.proyecto.proyectointegradomra.ui.theme.ColorEliminar

@Composable
fun SingUpView(
    singUpController: SignUpViewModel = viewModel(),
    dataRepository: DataRepository,
    navToLogIn: () -> Unit
) {
    val name by singUpController.name.observeAsState("")
    val email by singUpController.email.observeAsState("")
    val password by singUpController.password.observeAsState("")
    val repeatPassword by singUpController.repeatPassword.observeAsState("")
    val esOfertante by singUpController.esOfertante.observeAsState(false)
    var errorMessage by remember { mutableStateOf("") }
    var errorMessages by remember { mutableStateOf<List<String>>(emptyList()) }

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
            CampoDeTextoPorDefectoEditable(label = "Nombre de usuario",
                value = name,
                icon = Icons.Filled.AccountBox,
                onValueChange = { singUpController.updateName(it) })
        }
        item {
            CampoDeTextoPorDefectoEditable(
                label = "Correo electrónico",
                value = email,
                icon = Icons.Filled.Email,
                onValueChange = { singUpController.updateEmail(it) },
                keyboardType = KeyboardType.Email
            )
        }
        item {
            CampoDeTextoPorDefectoEditable(
                label = "Contraseña",
                value = password,
                onValueChange = { singUpController.updatePassword(it) },
                isPassword = true
            )
        }
        item {
            CampoDeTextoPorDefectoEditable(
                label = "Repita contraseña",
                value = repeatPassword,
                onValueChange = { singUpController.updateRepeatPassword(it) },
                isPassword = true
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = esOfertante,
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
        // Mensajes de error
        if (errorMessages.isNotEmpty()) {
            errorMessages.forEach { mensaje ->
                items(
                    listOf(mensaje)
                ) {
                    Text(
                        text = mensaje,
                        color = ColorEliminar,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
        if (errorMessage.isNotEmpty()) {
            item {
                Text(text = errorMessage, color = ColorEliminar)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            BotonPorDefecto(
                text = "Registrarse",
                icon = Icons.Filled.AccountBox,
                onClick = {
                    val errores = mutableListOf<String>()
                    if (name.isBlank()) errores.add("El nombre no puede estar vacío.")
                    if (email.isBlank()) errores.add("El email no puede estar vacío.")
                    if (password.isBlank()) errores.add("La contraseña no puede estar vacía.")
                    if (repeatPassword.isBlank()) errores.add("La contraseña no puede estar vacía.")
                    if ((password != repeatPassword) || (password.isEmpty())) errores.add("Las contraseñas no coinciden")

                    if (errores.isNotEmpty()) {
                        errorMessages = errores
                    } else {
                        dataRepository.registrarse(
                            email,
                            password,
                            name,
                            esOfertante,
                            onSuccess = {
                                navToLogIn()
                            },
                            onError = { exception ->
                                errorMessage = exception.message ?: "Error desconocido"
                            })
                    }
                })
        }

    }
}

