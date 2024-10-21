package com.proyecto.proyectointegradomra.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.Authentication.AuthController
import com.proyecto.proyectointegradomra.ui.theme.ColorDeFondo

@Preview()
@Composable
fun HomeView(
    homeController: HomeController = viewModel(),
    authController: AuthController = viewModel(),
 //   navTo: NavHostController
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(ColorDeFondo),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
}
