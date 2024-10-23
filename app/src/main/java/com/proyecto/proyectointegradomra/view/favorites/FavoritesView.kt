package com.proyecto.proyectointegradomra.view.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.Authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo
import com.proyecto.proyectointegradomra.view.BottomNavigationBar
import com.proyecto.proyectointegradomra.view.Logo
import com.proyecto.proyectointegradomra.view.home.HomeController

@Composable
fun FavoritesView(
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
            Text(text = "Favoritos")
        }
    }
}