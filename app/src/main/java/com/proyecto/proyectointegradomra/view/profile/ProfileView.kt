package com.proyecto.proyectointegradomra.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.proyecto.proyectointegradomra.authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.BottomNavigationBar
import com.proyecto.proyectointegradomra.view.Logo
import com.proyecto.proyectointegradomra.view.StandardButton
import com.proyecto.proyectointegradomra.view.home.HomeController

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    val navController = rememberNavController()

    ProfileView(navTo = navController)

}

@Composable
fun ProfileView(
    homeController: HomeController = viewModel(),
    authController: AuthController = viewModel(),
    navTo: NavHostController
) {
    Scaffold(bottomBar = { BottomNavigationBar(navController = navTo) }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(ColorDeFondo)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Text(text = "Perfil")
            StandardButton(text = "Cerrar Sesión", icon = Icons.Filled.AccountBox, onClick = {
                authController.cerrarSesion()
                navTo.navigate("StartScreen")
            })
        }
    }
}