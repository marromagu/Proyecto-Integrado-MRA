package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.view.start.StartScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = AppScreens.StartScreen.ruta) {
        composable(route = AppScreens.StartScreen.ruta) {
            StartScreen()
        }
    }

}