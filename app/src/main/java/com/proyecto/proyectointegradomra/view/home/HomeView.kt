package com.proyecto.proyectointegradomra.view.home

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyecto.proyectointegradomra.Authentication.AuthController


@Composable
fun HomeView(
    homeController: HomeController = viewModel(),
    authController: AuthController = viewModel(),
    navTo: NavHostController
) {

}