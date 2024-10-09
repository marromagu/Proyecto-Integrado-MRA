package com.proyecto.proyectointegradomra.view.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.ui.theme.Gris
import com.proyecto.proyectointegradomra.ui.theme.Negro
import com.proyecto.proyectointegradomra.ui.theme.Rojo
import com.proyecto.proyectointegradomra.ui.theme.Rosa
import com.proyecto.proyectointegradomra.ui.theme.Blanco


@Preview
@Composable
fun LogInScreen(
    viewModel: LogInViewModel = LogInViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Gris, Negro), startY = 0f, endY = 600f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoImagen()
        Registrador(modifier = Modifier.padding(32.dp, 16.dp), viewModel = viewModel)
    }

}

@Composable
fun Registrador(modifier: Modifier, viewModel: LogInViewModel) {
    val customTextField = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Rosa,
        unfocusedIndicatorColor = Rojo,
        cursorColor = Rosa
    )
    val texts  = KeyboardType.Text
    val password  = KeyboardType.Password

    val nombre by viewModel.nombre.observeAsState("")
    val contrasenna by viewModel.contrasenna.observeAsState("")
    Column(modifier = modifier) {
        CampoDeTexto(nombre, "Nombre", customTextField, texts){viewModel.updateNombre(it)}
        CampoDePassword(contrasenna, "Contraseña", customTextField, password){viewModel.updateContrasenna(it)}
        BotonDeRegistro()
    }

}

@Composable
fun BotonDeRegistro() {
    Box(
        modifier = Modifier
            .fillMaxWidth().padding(32.dp, 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Rojo)
        ) {
            Text(
                text = "Log In",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CampoDePassword(
    value: String,
    placeholder: String,
    customTextField: TextFieldColors,
    type: KeyboardType,
    onValueChange: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = {onValueChange(it)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp, 16.dp),
        placeholder = { Text(text = placeholder) },
        colors = customTextField,
        textStyle = TextStyle(color = Blanco),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = type),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = "Icono")
            }

        }
    )
}

@Composable
fun CampoDeTexto(
    value: String,
    placeholder: String,
    customTextField: TextFieldColors,
    type: KeyboardType,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = {onValueChange(it)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp, 16.dp),
        placeholder = { Text(text = placeholder) },
        colors = customTextField,
        textStyle = TextStyle(color = Blanco),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = type)
    )
}

@Composable
fun LogoImagen() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Logo",
        modifier = Modifier
            .size(200.dp)
            .padding(top = 16.dp)

    )
}