package com.proyecto.proyectointegradomra.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.navigation.Screens
import com.proyecto.proyectointegradomra.ui.theme.ColorContainer
import com.proyecto.proyectointegradomra.ui.theme.ColorDeBotones
import com.proyecto.proyectointegradomra.ui.theme.ColorDeLetras
import com.proyecto.proyectointegradomra.ui.theme.ColorFocuseado
import com.proyecto.proyectointegradomra.ui.theme.ColorUnfocuseado

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
fun StandardFieldText(
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
    val logoTipo = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier.padding(16.dp)) {
        Image(
            painter = logoTipo,
            contentDescription = "logo",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun FotoPerfil() {
    val fotoPerfil = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(modifier = Modifier) {
        Image(
            painter = fotoPerfil,
            contentDescription = "",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}