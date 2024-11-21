package com.proyecto.proyectointegradomra.ui.common

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.ParseException
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.data.model.*
import com.proyecto.proyectointegradomra.navigation.Screens
import com.proyecto.proyectointegradomra.repository.DataRepository
import com.proyecto.proyectointegradomra.ui.theme.*
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun Preview() {
}

// Función para validar los campos de entrada
fun validarCampos(
    title: String,
    description: String,
    fecha: String,
    hora: String,
    plazas: Int,
    participantes: Int
): List<String> {
    val errores = mutableListOf<String>()

    if (title.isBlank()) errores.add("El título no puede estar vacío.")
    if (description.isBlank()) errores.add("La descripción no puede estar vacía.")
    if (fecha.isBlank()) errores.add("Debes seleccionar una fecha.")
    if (hora.isBlank()) errores.add("Debes seleccionar una hora.")
    if (plazas <= 0) errores.add("Debes ingresar un número válido de plazas.")
    if (participantes > plazas) errores.add("No puedes tener menos plazas que participantes.")

    val fechaCombinada = combinarFechaYHora(fecha, hora)
    if (fechaCombinada == null) {
        errores.add("La fecha y hora no son válidas o son anteriores a la actual.")
    }

    return errores
}

// Función para actualizar la publicación
fun actualizarPublicacion(
    miPublicacion: Publicacion,
    miUsuario: Usuario?,
    title: String,
    description: String,
    plazas: Int,
    fecha: String,
    hora: String,
    dataRepository: DataRepository,
    navTo: NavHostController
) {
    val fechaCombinada = combinarFechaYHora(fecha, hora) ?: return

    miPublicacion.apply {
        ownerId = miUsuario?.uid ?: ""
        this.title = title
        this.description = description
        this.size = plazas
        this.date = fechaCombinada
        this.type = if (miUsuario?.type == TipoUsuarios.CONSUMIDOR) {
            TipoPublicaciones.BUSQUEDA
        } else {
            TipoPublicaciones.ACTIVIDAD
        }
    }

    dataRepository.actualizarPublicacion(miPublicacion)
    navTo.navigate("CreateView")
}

// Función para combinar fecha y hora en un formato de tiempo en milisegundos
fun combinarFechaYHora(fecha: String, hora: String): Long? {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    val fechaCompleta = "$fecha $hora"
    return try {
        val fechaSeleccionada = formatoFecha.parse(fechaCompleta)?.time
        val fechaActual = System.currentTimeMillis()
        if (fechaSeleccionada != null && fechaSeleccionada >= fechaActual) {
            fechaSeleccionada
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

// Define los colores personalizados para los campos de texto
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

// Componente para seleccionar una hora con un TimePicker y mostrarla en un campo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentanaHora(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
    defaultTime: String? = null
) {
    // Estado para controlar la visibilidad del TimePicker
    var showTimePicker by remember { mutableStateOf(false) }
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val defaultCalendar = Calendar.getInstance()

    // Parsear hora predeterminada si está disponible
    defaultTime?.let {
        try {
            defaultCalendar.time = timeFormatter.parse(it)!!
        } catch (e: ParseException) {
            Log.e("VentanaHora", "Error al analizar la hora: ${e.message}")
        }
    }

    // Estado para almacenar la hora seleccionada
    var selectedTime by remember {
        mutableStateOf(
            defaultTime ?: timeFormatter.format(defaultCalendar.time)
        )
    }

    // Campo de texto de solo lectura que muestra la hora seleccionada
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

    // Mostrar el TimePicker en un cuadro de diálogo
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = defaultCalendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = defaultCalendar.get(Calendar.MINUTE)
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
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
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = ColorDeBotones)
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = ColorDeBotones)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Componente para seleccionar una fecha con un DatePicker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentanaFecha(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
    defaultDate: String? = null
) {
    // Estado para controlar la visibilidad del DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val defaultCalendar = Calendar.getInstance()

    // Parsear fecha predeterminada si está disponible
    defaultDate?.let {
        defaultCalendar.time = dateFormatter.parse(it)!!
    }

    // Estado para almacenar la fecha seleccionada
    var selectedDate by remember {
        mutableStateOf(
            defaultDate ?: dateFormatter.format(defaultCalendar.time)
        )
    }

    // Campo de texto de solo lectura que muestra la fecha seleccionada
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha") },
        leadingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar Fecha")
            }
        },
        colors = miTextFieldColors(),
        modifier = modifier.clickable { showDatePicker = true }
    )

    // Mostrar el DatePicker en un cuadro de diálogo
    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = defaultCalendar.timeInMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    selectedDate = selectedMillis?.let {
                        dateFormatter.format(Date(it))
                    } ?: ""
                    onDateSelected(selectedDate)
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun BarraDeNavegacion(navController: NavHostController) {
    val currentRoute = currentRouteBarraDeNavegacion(navController)
    val defaultColors = NavigationBarItemDefaults.colors(
        selectedIconColor = ColorDeBotones,
        unselectedIconColor = ColorDeBotones,
        selectedTextColor = ColorDeBotones,
        unselectedTextColor = ColorDeBotones,
        indicatorColor = ColorIconoBotones
    )
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddBox, contentDescription = "Crear") },
            label = { Text("Crear") },
            selected = currentRoute == Screens.CreateScreen.ruta,
            onClick = {
                navController.navigate(Screens.CreateScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }, colors = defaultColors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screens.HomeScreen.ruta,
            onClick = {
                navController.navigate(Screens.HomeScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }, colors = defaultColors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Perfil") },
            selected = currentRoute == Screens.ProfileScreen.ruta,
            onClick = {
                navController.navigate(Screens.ProfileScreen.ruta) {
                    popUpTo(Screens.HomeScreen.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }, colors = defaultColors
        )
    }
}

fun currentRouteBarraDeNavegacion(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}

@Composable
fun CampoNumeroDePlazas(value: Int, onValueChange: (Int) -> Unit) {
    Row {
        IconButton(
            onClick = {
                if (value <= 0) {
                    onValueChange(0)
                } else {
                    onValueChange(value - 1)
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
            value = value.toString(),
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
            onClick = {
                if (value < 999) {
                    onValueChange(value + 1)
                } else {
                    onValueChange(999)
                }
            }, modifier = Modifier.padding(8.dp)
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
    miPublicacion: Publicacion,
    action: String = "",
    onItemClick: () -> Unit = {},
    onItemClickDelete: () -> Unit
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
            .padding(8.dp),

        ) {
        Box(Modifier.fillMaxSize()) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = miPublicacion.title,
                        modifier = Modifier
                            .padding(8.dp)
                            .width(200.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    if (expanded) {
                        Spacer(modifier = Modifier.weight(1f))
                        when (action) {
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

                            "update" -> {
                                IconButton(onClick = {
                                    onItemClickDelete()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.DeleteForever,
                                        contentDescription = "",
                                        tint = ColorEliminar,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .align(Alignment.Bottom)
                                    )
                                }
                                IconButton(onClick = {
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
                        text = "Plazas: ${miPublicacion.participantes.size}/${miPublicacion.size}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DialogoAlertaBotones(
    showDialog: Boolean, message: String, onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(onDismissRequest = { onDismiss() },
            title = { Text("Confirmación") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
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
fun DialogoAlerta(
    showAlert: Boolean, alertMessage: String, actionConfirmed: () -> Unit, onDismiss: () -> Unit
) {
    if (showAlert) {
        AlertDialog(onDismissRequest = onDismiss,
            title = { Text("Error") },
            text = { Text(alertMessage) },
            confirmButton = {
                Button(onClick = {
                    actionConfirmed()
                    onDismiss()
                }) {
                    Text("Aceptar")
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
                    onValueChange = { newValue ->
                        if (newValue.length <= 50) {
                            newNameChanger(newValue)
                        }
                    },
                    label = { Text("Nuevo nombre") })
            },
            confirmButton = {
                Button(onClick = {
                    if (newName.isNotBlank()) {
                        dataRepository.actualizarNombreUsuario(uid ?: "", newName)
                        onDismiss()
                        newNameChanger("")
                        showDialogChanger(false)
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
fun CampoDeTextoEnArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int = 250
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        label = { Text(label) },
        colors = miTextFieldColors(),
        keyboardActions = KeyboardActions(onDone = { }),
    )
}

@Composable
fun CampoDeTextoPorDefectoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null,
    maxLength: Int? = 35
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength!!) {
                onValueChange(newValue)
            }
        },
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
    label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier
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
            disabledContainerColor = ColorContainer,
            disabledTextColor = ColorDeLetras,
            disabledLabelColor = ColorDeLetras,
            disabledIndicatorColor = ColorDeLetras
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
    val img = painterResource(id = R.drawable.natural)
    Box(modifier = Modifier.padding(16.dp, 32.dp, 16.dp, 8.dp)) {
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
    val img = painterResource(id = R.drawable.giralda)
    Box(modifier = Modifier.padding(16.dp, 32.dp, 16.dp, 8.dp), Alignment.Center) {
        Image(
            painter = img,
            contentDescription = "Logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun CrearIMG() {
    val img = painterResource(id = R.drawable.mapa_del_tesoro)
    Box(modifier = Modifier.padding(16.dp, 32.dp, 16.dp, 8.dp), Alignment.Center) {
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
    val img = painterResource(id = R.drawable.perfil)
    Box(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = img,
            contentDescription = "Error",
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}