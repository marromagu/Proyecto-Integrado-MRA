package com.proyecto.proyectointegradomra.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.proyectointegradomra.view.start.StartScreen
import com.proyecto.proyectointegradomra.view.singUp.SingUpScreen
import com.proyecto.proyectointegradomra.view.login.LogInScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppScreens.StartScreen.ruta) {
        composable(route = AppScreens.StartScreen.ruta) {
            StartScreen(
                navToSignUp = { navController.navigate(AppScreens.SignUpScreen.ruta) },
                navToLogIn = { navController.navigate(AppScreens.LogInScreen.ruta) }
            )
        }
        composable(route = AppScreens.SignUpScreen.ruta) {
            SingUpScreen()
        }
        composable(route = AppScreens.LogInScreen.ruta) {
            LogInScreen()
        }
    }
}