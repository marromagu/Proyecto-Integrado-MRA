package com.proyecto.proyectointegradomra.navigation

sealed class AppScreens(val ruta: String){
    object StartScreen: AppScreens("start_screen")
    object LogInScreen: AppScreens("login_screen")
    object SignUpScreen: AppScreens("signup_screen")
    object HomeScreen: AppScreens("home_screen")
}