package com.proyecto.proyectointegradomra.view

import androidx.compose.material3.CheckboxDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.navigation.Screens
import com.proyecto.proyectointegradomra.ui.theme.ColorContainer
import com.proyecto.proyectointegradomra.ui.theme.ColorDeBotones
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras
import com.proyecto.proyectointegradomra.ui.theme.ColorFocuseado
import com.proyecto.proyectointegradomra.ui.theme.ColorUnfocuseado
import com.proyecto.proyectointegradomra.ui.theme.VerdeClaro

//@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Nombre de usuario")
                IconButton(onClick = { /* Abrir diálogo o pantalla para editar el nombre */ }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Editar nombre")
                }
                FotoPerfil()
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.padding(90.dp, 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StandardButton(text = "Cerrar Sesión",
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    onClick = {
                    })
                StandardButton(text = "Borrar Cuenta",
                    icon = Icons.Filled.DeleteForever,
                    onClick = {
                    })
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewStandardField() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorDeFondo)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Logo() }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        item {
            StandardField(
                label = "Nombre de usuario",
                value = "nombre",
                icon = Icons.Filled.AccountBox,
                onValueChange = { }
            )
        }
        item {
            StandardField(
                label = "Correo electrónico",
                value = "correo",
                icon = Icons.Filled.Email,
                onValueChange = { },
                keyboardType = KeyboardType.Email
            )
        }
        item {
            StandardField(
                label = "Contraseña",
                value = "contrasena",
                onValueChange = { },
                isPassword = true
            )
        }
        item {
            StandardField(
                label = "Repita contraseña",
                value = "repetirContrasena",
                onValueChange = { },
                isPassword = true
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = { },
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
                    interactionSource = remember { MutableInteractionSource() }
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Registrar como Ofertante.",
                    style = TextStyle(
                        color = ColorDeLetras,
                    )
                )
            }
        }
        item { Spacer(modifier = Modifier.height(6.dp)) }
        item {
            StandardButton(
                text = "Registrarse",
                icon = Icons.Filled.AccountBox,
                onClick = {}
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = currentRoute(navController)

    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos") },
            selected = currentRoute == Screens.FavoritesScreen.ruta,
            onClick = {
                navController.navigate(Screens.FavoritesScreen.ruta) {
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


fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}

@Composable
fun StandardField(
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
fun StandardButton(
    text: String, icon: ImageVector, onClick: () -> Unit
) {
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
fun Logo() {
    val logotipo = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = logotipo,
            contentDescription = "logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FotoPerfil() {
    val logotipo = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier) {
        Image(
            painter = logotipo,
            contentDescription = "logo",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}