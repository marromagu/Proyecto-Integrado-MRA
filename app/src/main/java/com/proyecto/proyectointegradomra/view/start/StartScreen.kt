package com.proyecto.proyectointegradomra.view.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import com.proyecto.proyectointegradomra.R
import com.proyecto.proyectointegradomra.ui.theme.Gris
import com.proyecto.proyectointegradomra.ui.theme.Negro
import com.proyecto.proyectointegradomra.ui.theme.Rojo

@Preview
@Composable
fun StartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Gris, Negro), startY = 0f, endY = 600f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoImagen()
        Texto()
        Spacer(modifier = Modifier.weight(1f))
        SignUp()
        LogIn()
    }
}

@Composable
fun LogIn() {
    Text(
        text = "Iniciar Seccion",
        modifier = Modifier.padding(16.dp),
        style = TextStyle(
            textDecoration = TextDecoration.Underline,
            color = Color.White,
            fontSize = 16.sp
        )
    )
}

@Composable
fun SignUp() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.6f),
        colors = ButtonDefaults.buttonColors(containerColor = Rojo)
    ) {
        Text(
            text = "Sign Up Free",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun Texto() {
    Text(
        text = "Bienvenido",
        style = TextStyle(
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(top = 16.dp)
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
