package com.proyecto.proyectointegradomra.ui.common

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.GroupRemove
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.data.model.Publicaciones
import com.proyecto.proyectointegradomra.navigation.Screens
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.ColorContainer
import com.proyecto.proyectointegradomra.ui.theme.ColorDeBotones
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras
import com.proyecto.proyectointegradomra.ui.theme.ColorEliminar
import com.proyecto.proyectointegradomra.ui.theme.ColorFocuseado
import com.proyecto.proyectointegradomra.ui.theme.ColorIconoBotones
import com.proyecto.proyectointegradomra.ui.theme.ColorUnfocuseado
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun Preview() {
}

@Composable
fun miTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = ColorContainer,
        unfocusedContainerColor = ColorContainer,
        focusedIndicatorColor = ColorFocuseado,
        unfocusedIndicatorColor = ColorUnfocuseado,
        focusedTextColor = ColorDeLetras,
        unfocusedTextColor = ColorUnfocuseado,
        focusedLabelColor = ColorFocuseado,
        unfocusedLabelColor = ColorUnfocuseado,
        focusedTrailingIconColor = ColorFocuseado,
        unfocusedTrailingIconColor = ColorUnfocuseado,
        focusedLeadingIconColor = ColorFocuseado,
        unfocusedLeadingIconColor = ColorUnfocuseado,
        unfocusedPlaceholderColor = ColorUnfocuseado,
        focusedPlaceholderColor = ColorFocuseado,
        cursorColor = ColorFocuseado
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentanaHora(
    modifier: Modifier = Modifier, onDateSelected: (String) -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        readOnly = true,
        label = { Text("Hora") },
        leadingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(Icons.Filled.Schedule, contentDescription = "Seleccionar Hora")
            }
        },
        colors = miTextFieldColors(),
        modifier = modifier
    )

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(onDismissRequest = { showTimePicker = false },
            title = { Text("Selecciona la hora") },
            text = {
                Column {
                    TimePicker(
                        state = timePickerState, colors = TimePickerDefaults.colors(
                            periodSelectorSelectedContainerColor = ColorContainer,
                            timeSelectorSelectedContainerColor = ColorContainer,
                            selectorColor = ColorDeBotones
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cal = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                        }
                        selectedTime = timeFormatter.format(cal.time)
                        onDateSelected(selectedTime)
                        showTimePicker = false
                    }, colors = ButtonDefaults.textButtonColors(
                        contentColor = ColorDeBotones
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }, colors = ButtonDefaults.textButtonColors(
                        contentColor = ColorDeBotones
                    )
                ) {
                    Text("Cancelar")
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentanaFecha(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    OutlinedTextField(value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha") },
        leadingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar Fecha")
            }
        },
        colors = miTextFieldColors(),
        modifier = modifier.clickable { showDatePicker = true })

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                val selectedMillis = datePickerState.selectedDateMillis
                selectedDate = selectedMillis?.let {
                    dateFormatter.format(Date(it))
                } ?: ""

                // Llama a onDateSelected con la fecha seleccionada
                onDateSelected(selectedDate)

                showDatePicker = false
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePicker = false }) {
                Text("Cancelar")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun BarraDeNavegacion(navController: NavHostController) {
    val currentRoute = currentRouteBarraDeNavegacion(navController)

    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Filled.AddBox, contentDescription = "Crear") },
            label = { Text("Crear") },
            selected = currentRoute == Screens.CreateScreen.ruta,
            onClick = {
                navController.navigate(Screens.CreateScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            })
        NavigationBarItem(icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screens.HomeScreen.ruta,
            onClick = {
                navController.navigate(Screens.HomeScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            })
        NavigationBarItem(icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Perfil") },
            selected = currentRoute == Screens.ProfileScreen.ruta,
            onClick = {
                navController.navigate(Screens.ProfileScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            })
    }
}

fun currentRouteBarraDeNavegacion(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}

@Composable
fun CampoNumeroDePlazas(plazas: Int, onValueChange: (Int) -> Unit) {
    Row {
        IconButton(
            onClick = {
                if (plazas <= 0) {
                    onValueChange(0)
                } else {
                    onValueChange(plazas - 1)
                }
            }, modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                Icons.Filled.ArrowCircleDown,
                contentDescription = "-",
                tint = ColorDeLetras,
                modifier = Modifier.size(25.dp)
            )
        }
        OutlinedTextField(
            value = plazas.toString(),
            label = { Text("Plazas") },
            onValueChange = { onValueChange(it.toIntOrNull() ?: 0) },
            colors = miTextFieldColors(),
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp)
                .width(100.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        IconButton(
            onClick = { onValueChange(plazas + 1) }, modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                Icons.Filled.ArrowCircleUp,
                contentDescription = "+",
                tint = ColorDeLetras,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun CardClickable(
    miPublicacion: Publicaciones, isAdd: String = "", onItemClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val cardSize by animateDpAsState(
        targetValue = if (expanded) 400.dp else 150.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    ElevatedCard(
        onClick = { expanded = !expanded },
        modifier = Modifier
            .size(width = 350.dp, height = cardSize)
            .padding(8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = miPublicacion.title, modifier = Modifier.padding(8.dp)
                    )
                    if (expanded) {
                        Spacer(modifier = Modifier.weight(1f))
                        when (isAdd) {
                            "add" -> IconButton(onClick = {
                                onItemClick()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.PersonAdd,
                                    contentDescription = "",
                                    tint = ColorIconoBotones,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Bottom)
                                )
                            }

                            "remove" -> IconButton(onClick = {
                                onItemClick()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.GroupRemove,
                                    contentDescription = "",
                                    tint = ColorEliminar,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Bottom)
                                )
                            }

                            "update" -> IconButton(onClick = {
                                onItemClick()
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.BorderColor,
                                    contentDescription = "",
                                    tint = ColorIconoBotones,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Bottom)
                                )
                            }
                        }
                    }
                }
                if (expanded) {
                    Text(
                        text = miPublicacion.description,
                        modifier = Modifier.padding(16.dp, 16.dp, 8.dp, 4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    val formatoFecha =
                        java.text.SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                    val fechaCompleta = formatoFecha.format(Date(miPublicacion.date))
                    Text(
                        text = fechaCompleta, modifier = Modifier.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Plazas: ${miPublicacion.participantes.size}/${miPublicacion.plazas}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DialogoAlerta(
    showAlert: Boolean, alertMessage: String, actionConfirmed: () -> Unit, onDismiss: () -> Unit
) {
    if (showAlert) {
        AlertDialog(onDismissRequest = { onDismiss() },
            title = { Text("Confirmación") },
            text = { Text(alertMessage) },
            confirmButton = {
                Button(onClick = {
                    actionConfirmed()
                    onDismiss()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text("Cancelar")
                }
            })
    }
}

@Composable
fun DialogoEditarNombre(
    showDialog: Boolean,
    showDialogChanger: (Boolean) -> Unit,
    newName: String,
    uid: String?,
    newNameChanger: (String) -> Unit,
    dataRepository: DataRepository,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialogChanger(false) },
            title = { Text("Editar nombre") },
            text = {
                OutlinedTextField(value = newName,
                    onValueChange = { newNameChanger(it) },
                    label = { Text("Nuevo nombre") })
            },
            confirmButton = {
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        dataRepository.actualizarNombreUsuario(uid ?: "", newName)
                        newNameChanger("")
                        showDialogChanger(false)
                        onDismiss()
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialogChanger(false) }) {
                    Text("Cancelar")
                }
            })
    }
}

@Composable
fun CampoDeTextoEnArea(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        label = { Text(label) },
        colors = miTextFieldColors(),
    )
}

@Composable
fun CampoDeTextoPorDefectoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ColorContainer,
            unfocusedContainerColor = ColorContainer,
            focusedIndicatorColor = ColorFocuseado,
            unfocusedIndicatorColor = ColorUnfocuseado,
            focusedTextColor = ColorDeLetras,
            unfocusedTextColor = ColorUnfocuseado,
            focusedLabelColor = ColorFocuseado,
            unfocusedLabelColor = ColorUnfocuseado,
            focusedTrailingIconColor = ColorFocuseado,
            unfocusedTrailingIconColor = ColorUnfocuseado,
            cursorColor = ColorFocuseado
        ),
        trailingIcon = {
            when {
                isPassword -> {
                    val visibilityIcon =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = visibilityIcon, contentDescription = null
                        )
                    }
                }

                icon != null -> {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        })
}

@Composable
fun CampoDeTextoPorDefectoNoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = true,
        enabled = false,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            disabledContainerColor = ColorContainer
        )
    )
}

@Composable
fun BotonPorDefecto(text: String, icon: ImageVector, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(16.dp)) {
        ExtendedFloatingActionButton(modifier = Modifier.size(300.dp, 60.dp),
            containerColor = ColorDeBotones,
            contentColor = ColorDeLetras,
            onClick = { onClick() }) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = text, style = TextStyle(
                    fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun PublicacionIMG() {
    val img = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier) {
        Image(
            painter = img,
            contentDescription = "Error",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun Logo() {
    val img = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = img,
            contentDescription = "Logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun Foto() {
    val img = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier) {
        Image(
            painter = img,
            contentDescription = "Error",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}